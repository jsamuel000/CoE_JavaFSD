import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class Book implements Serializable {
    private String title;
    private String author;
    private String ISBN;
    private boolean isBorrowed;
    private String reservedBy;

    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.isBorrowed = false;
        this.reservedBy = null;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }
    public boolean isBorrowed() { return isBorrowed; }
    public String getReservedBy() { return reservedBy; }

    public void borrow() { isBorrowed = true; }
    public void returnBook() { isBorrowed = false; }
    public void reserve(String userID) { reservedBy = userID; }
    public void clearReservation() { reservedBy = null; }

    public String toString() {
        return title + " by " + author + " (ISBN: " + ISBN + ") " + 
               (isBorrowed ? "[Borrowed]" : "[Available]") + 
               (reservedBy != null ? " [Reserved by " + reservedBy + "]" : "");
    }
}

class User implements Serializable {
    private String name;
    private String userID;
    private List<Book> borrowedBooks;

    public User(String name, String userID) {
        this.name = name;
        this.userID = userID;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getUserID() { return userID; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    public void borrowBook(Book book) { borrowedBooks.add(book); }
    public void returnBook(Book book) { borrowedBooks.remove(book); }

    public String toString() {
        return "User: " + name + " (ID: " + userID + ") - Borrowed: " + borrowedBooks.size();
    }
}

class BookNotFoundException extends Exception { public BookNotFoundException(String msg) { super(msg); } }
class UserNotFoundException extends Exception { public UserNotFoundException(String msg) { super(msg); } }
class MaxBooksAllowedException extends Exception { public MaxBooksAllowedException(String msg) { super(msg); } }

interface ILibrary {
    void borrowBook(String ISBN, String userID) throws BookNotFoundException, UserNotFoundException, MaxBooksAllowedException;
    void returnBook(String ISBN, String userID) throws BookNotFoundException, UserNotFoundException;
    void reserveBook(String ISBN, String userID) throws BookNotFoundException, UserNotFoundException;
    Book searchBook(String title);
}

abstract class LibrarySystem implements ILibrary {
    protected List<Book> books;
    protected List<User> users;

    public LibrarySystem() {
        books = new CopyOnWriteArrayList<>();
        users = new CopyOnWriteArrayList<>();
    }

    public abstract void addBook(Book book);
    public abstract void addUser(User user);
}

class LibraryManager extends LibrarySystem {
    private static final int MAX_BORROW_LIMIT = 3;
    private ExecutorService executor;

    public LibraryManager() {
        executor = Executors.newFixedThreadPool(3);
    }

    public synchronized void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book);
    }

    public synchronized void addUser(User user) {
        users.add(user);
        System.out.println("User added: " + user);
    }

    public void borrowBook(String ISBN, String userID) throws BookNotFoundException, UserNotFoundException, MaxBooksAllowedException {
        executor.execute(() -> {
            try {
                User user = users.stream().filter(u -> u.getUserID().equals(userID)).findFirst()
                        .orElseThrow(() -> new UserNotFoundException("User not found!"));

                Book book = books.stream().filter(b -> b.getISBN().equals(ISBN) && !b.isBorrowed()).findFirst()
                        .orElseThrow(() -> new BookNotFoundException("Book not available!"));

                if (user.getBorrowedBooks().size() >= MAX_BORROW_LIMIT) {
                    throw new MaxBooksAllowedException("Max borrow limit reached!");
                }

                book.borrow();
                user.borrowBook(book);
                System.out.println(user.getName() + " borrowed " + book.getTitle());

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public void returnBook(String ISBN, String userID) throws BookNotFoundException, UserNotFoundException {
        executor.execute(() -> {
            try {
                User user = users.stream().filter(u -> u.getUserID().equals(userID)).findFirst()
                        .orElseThrow(() -> new UserNotFoundException("User not found!"));

                Book book = user.getBorrowedBooks().stream().filter(b -> b.getISBN().equals(ISBN)).findFirst()
                        .orElseThrow(() -> new BookNotFoundException("Book not found in user records!"));

                book.returnBook();
                user.returnBook(book);
                System.out.println(user.getName() + " returned " + book.getTitle());

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public void reserveBook(String ISBN, String userID) throws BookNotFoundException, UserNotFoundException {
        executor.execute(() -> {
            try {
                User user = users.stream().filter(u -> u.getUserID().equals(userID)).findFirst()
                        .orElseThrow(() -> new UserNotFoundException("User not found!"));

                Book book = books.stream().filter(b -> b.getISBN().equals(ISBN)).findFirst()
                        .orElseThrow(() -> new BookNotFoundException("Book not found!"));

                book.reserve(userID);
                System.out.println(user.getName() + " reserved " + book.getTitle());

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public Book searchBook(String title) {
        return books.stream().filter(b -> b.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void displayLibrary() {
        System.out.println("\nLibrary Books:");
        books.forEach(System.out::println);
        System.out.println("\nRegistered Users:");
        users.forEach(System.out::println);
    }
}

public class UniversityLibrarySystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryManager library = new LibraryManager();

        System.out.println("===== UNIVERSITY LIBRARY SYSTEM =====");

        System.out.print("Enter number of books to add: ");
        int numBooks = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numBooks; i++) {
            System.out.print("Enter Book Title: ");
            String title = scanner.nextLine();

            System.out.print("Enter Author: ");
            String author = scanner.nextLine();

            System.out.print("Enter ISBN: ");
            String ISBN = scanner.nextLine();

            library.addBook(new Book(title, author, ISBN));
        }

        System.out.print("\nEnter number of users to register: ");
        int numUsers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numUsers; i++) {
            System.out.print("Enter User Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter User ID: ");
            String userID = scanner.nextLine();

            library.addUser(new User(name, userID));
        }

        library.displayLibrary();
        library.shutdown();
        scanner.close();
    }
}
