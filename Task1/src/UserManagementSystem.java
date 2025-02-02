import java.io.*;

class User {
	private String name;
	private String email;

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	@Override
	public String toString() {
		return name + ", " + email;
	}
}

class UserManager {
	private User[] users = new User[100];
	private int userCount = 0;

	public void addUser(String name, String email) {
		if (userCount < users.length) {
			users[userCount++] = new User(name, email);
		}
	}

	public void saveUsersToFile(String filename) {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8")) {
			for (int i = 0; i < userCount; i++) {
				writer.write(users[i].toString() + "\n");
			}
			System.out.println("Users saved successfully.");
		} catch (IOException e) {
			System.out.println("Error saving users: " + e.getMessage());
		}
	}

	public void loadUsersFromFile(String filename) {
		userCount = 0;
		try (Reader reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(reader)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts.length == 2) {
					addUser(parts[0], parts[1]);
				}
			}
			System.out.println("Users loaded successfully.");
		} catch (IOException e) {
			System.out.println("Error loading users: " + e.getMessage());
		}
	}

	public void displayUsers() {
		for (int i = 0; i < userCount; i++) {
			System.out.println(users[i]);
		}
	}
}

public class UserManagementSystem {
	public static void main(String[] args) {
		UserManager manager = new UserManager();
		manager.addUser("sam", "sam@example.com");
		manager.addUser("hi", "hi@example.com");

		String filename = "users.txt";
		manager.saveUsersToFile(filename);
		manager.loadUsersFromFile(filename);
		manager.displayUsers();
	}
}
