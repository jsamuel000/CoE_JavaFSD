class BankAccount {
	private double balance;

	public BankAccount(double initialBalance) {
		this.balance = initialBalance;
	}

	public synchronized void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
			System.out.println(Thread.currentThread().getName() + " deposited: " + amount + " | Balance: " + balance);
		}
	}

	public synchronized void withdraw(double amount) {
		if (amount > 0 && amount <= balance) {
			balance -= amount;
			System.out.println(Thread.currentThread().getName() + " withdrew: " + amount + " | Balance: " + balance);
		} else {
			System.out.println(
					Thread.currentThread().getName() + " failed to withdraw: " + amount + " | Insufficient funds.");
		}
	}
}

class BankUser implements Runnable {
	private final BankAccount account;
	private final double depositAmount;
	private final double withdrawAmount;

	public BankUser(BankAccount account, double depositAmount, double withdrawAmount) {
		this.account = account;
		this.depositAmount = depositAmount;
		this.withdrawAmount = withdrawAmount;
	}

	@Override
	public void run() {
		account.deposit(depositAmount);
		account.withdraw(withdrawAmount);
	}
}

public class BankSimulation {
	public static void main(String[] args) {
		BankAccount sharedAccount = new BankAccount(300);

		Thread user1 = new Thread(new BankUser(sharedAccount, 150, 100), "User1");
		Thread user2 = new Thread(new BankUser(sharedAccount, 200, 250), "User2");

		user1.start();
		user2.start();
	}
}
