import java.sql.*;
import java.util.Scanner;

public class FeeReportSoftware {
    private static final String URL = "jdbc:mysql://localhost:3306/FeeReportDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; 
    private static Connection conn;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            while (true) {
                System.out.println("\n===== Fee Report Software =====");
                System.out.println("1. Admin Login");
                System.out.println("2. Accountant Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> adminLogin();
                    case 2 -> accountantLogin();
                    case 3 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }
    }

    private static void adminLogin() throws SQLException {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter Admin Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Admin Password: ");
            String password = scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM admin WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Admin Login Successful!");
                adminPanel();
                return; 
            } else {
                System.out.println("Invalid Credentials. Try again.");
                attempts++;
                if (attempts == 3) {
                    System.out.println("Max login attempts reached. Exiting...");
                    System.exit(0); 
                }
            }
        }
    }

    private static void adminPanel() throws SQLException {
        while (true) {
            System.out.println("\n===== Admin Panel =====");
            System.out.println("1. Add Accountant");
            System.out.println("2. View Accountants");
            System.out.println("3. Edit Accountant");
            System.out.println("4. Delete Accountant");
            System.out.println("5. Manage Students");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addAccountant();
                case 2 -> viewAccountants();
                case 3 -> editAccountant();
                case 4 -> deleteAccountant();
                case 5 -> manageStudents();
                case 6 -> {
                    System.out.println("Logging out...");
                    return; 
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addAccountant() throws SQLException {
        System.out.print("Enter Accountant Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        if (!email.contains("@")) {
            System.out.println("Invalid email format.");
            return;
        }
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        if (phone.length() != 10) {
            System.out.println("Invalid phone number. Should be 10 digits.");
            return;
        }
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO accountant (name, email, phone, password) VALUES (?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.setString(4, password);
        ps.executeUpdate();
        System.out.println("Accountant added successfully!");
    }

    private static void viewAccountants() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM accountant");

        System.out.println("\n===== Accountant List =====");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                    ", Email: " + rs.getString("email") + ", Phone: " + rs.getString("phone"));
        }
    }

    private static void editAccountant() throws SQLException {
        System.out.print("Enter Accountant ID to Edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM accountant WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            System.out.println("Accountant not found.");
            return;
        }

        System.out.print("Enter New Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter New Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter New Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter New Password: ");
        String password = scanner.nextLine();

        ps = conn.prepareStatement("UPDATE accountant SET name=?, email=?, phone=?, password=? WHERE id=?");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.setString(4, password);
        ps.setInt(5, id);
        ps.executeUpdate();
        System.out.println("Accountant details updated successfully!");
    }

    private static void deleteAccountant() throws SQLException {
        System.out.print("Enter Accountant ID to Delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM accountant WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            System.out.println("Accountant not found.");
            return;
        }

        ps = conn.prepareStatement("DELETE FROM accountant WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Accountant deleted successfully!");
    }

    private static void manageStudents() throws SQLException {
        while (true) {
            System.out.println("\n===== Student Management =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Edit Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Go Back");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudents();
                case 3 -> editStudent();
                case 4 -> deleteStudent();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addStudent() throws SQLException {
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        if (!email.contains("@")) {
            System.out.println("Invalid email format.");
            return;
        }
        System.out.print("Enter Course: ");
        String course = scanner.nextLine();
        System.out.print("Enter Total Fee: ");
        double fee = scanner.nextDouble();
        if (fee < 0) {
            System.out.println("Fee cannot be negative.");
            return;
        }
        System.out.print("Enter Amount Paid: ");
        double paid = scanner.nextDouble();
        if (paid < 0) {
            System.out.println("Amount paid cannot be negative.");
            return;
        }
        double due = fee - paid;
        scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO student (name, email, course, fee, paid, due) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, course);
        ps.setDouble(4, fee);
        ps.setDouble(5, paid);
        ps.setDouble(6, due);
        ps.executeUpdate();
        System.out.println("Student added successfully!");
    }

    private static void viewStudents() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM student");

        System.out.println("\n===== Student List =====");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Course: " +
                    rs.getString("course") + ", Fee: " + rs.getDouble("fee") + ", Paid: " +
                    rs.getDouble("paid") + ", Due: " + rs.getDouble("due"));
        }
    }

    private static void editStudent() throws SQLException {
        System.out.print("Enter Student ID to Edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM student WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter New Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter New Course: ");
        String course = scanner.nextLine();
        System.out.print("Enter New Address: ");
        String address = scanner.nextLine();

        ps = conn.prepareStatement("UPDATE student SET name=?, course=?, address=? WHERE id=?");
        ps.setString(1, name);
        ps.setString(2, course);
        ps.setString(3, address);
        ps.setInt(4, id);
        ps.executeUpdate();
        System.out.println("Student details updated successfully!");
    }

    private static void deleteStudent() throws SQLException {
        System.out.print("Enter Student ID to Delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM student WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            System.out.println("Student not found.");
            return;
        }

        ps = conn.prepareStatement("DELETE FROM student WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Student deleted successfully!");
    }

    private static void accountantLogin() throws SQLException {
        System.out.print("Enter Accountant Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Accountant Password: ");
        String password = scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM accountant WHERE email=? AND password=?");
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Accountant Login Successful!");
            accountantPanel();
        } else {
            System.out.println("Invalid Credentials. Try again.");
        }
    }

    private static void accountantPanel() throws SQLException {
        while (true) {
            System.out.println("\n===== Accountant Panel =====");
            System.out.println("1. View Students");
            System.out.println("2. Manage Fees");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewStudents();
                case 2 -> manageFees();
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void manageFees() throws SQLException {
        System.out.print("Enter Student ID to Manage Fees: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM student WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            System.out.println("Student not found.");
            return;
        }

        double fee = rs.getDouble("fee");
        double paid = rs.getDouble("paid");
        double due = rs.getDouble("due");

        System.out.println("Fee: " + fee + ", Paid: " + paid + ", Due: " + due);
        System.out.print("Enter Amount to Pay: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount < 0) {
            System.out.println("Amount cannot be negative.");
            return;
        }

        double newPaid = paid + amount;
        double newDue = fee - newPaid;

        ps = conn.prepareStatement("UPDATE student SET paid=?, due=? WHERE id=?");
        ps.setDouble(1, newPaid);
        ps.setDouble(2, newDue);
        ps.setInt(3, id);
        ps.executeUpdate();
        System.out.println("Fees updated successfully!");
    }
}
