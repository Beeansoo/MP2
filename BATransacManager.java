import java.util.Scanner;

public class BAtransacManager {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int accountCount = readIntInRange(input, "
        Number of accounts (1-5): ", 1, 5);
        BankAccount[] accounts = new BankAccount[accountCount];

        for (int i = 0; i < accounts.length; i++) {
            System.out.println("\nAccount " + (i + 1));
            System.out.print("Account number: ");
            String number = input.nextLine().trim();
            System.out.print("Owner name: ");
            String owner = input.nextLine().trim();
            double opening = readNonNegativeDouble(input, "Opening balance: ");
            accounts[i] = new BankAccount(number, owner, opening);
        }

        int transactions = readNonNegativeInt(input, "\nNumber of transactions: ");
        for (int i = 0; i < transactions; i++) {
            System.out.println("\nTransaction " + (i + 1));
            System.out.print("Account number: ");
            String number = input.nextLine().trim();
            BankAccount target = findAccount(accounts, number);
            if (target == null) {
                System.out.println("Rejected: account not found.");
                continue;
            }

            System.out.print("Type (D = deposit, W = withdrawal): ");
            String type = input.nextLine().trim().toUpperCase();
            double amount = readDouble(input, "Amount: ");
            boolean successful;
            if (type.equals("D")) {
                successful = target.deposit(amount);
            } else if (type.equals("W")) {
                successful = target.withdraw(amount);
            } else {
                System.out.println("Rejected: invalid transaction type.");
                continue;
            }
            System.out.println(successful ? "Transaction successful." : "Rejected: invalid amount or insufficient balance.");
        }

        System.out.println("\nFINAL BALANCES");
        for (BankAccount account : accounts) {
            System.out.printf("%s - %s: %.2f%n", account.getAccountNumber(), account.getOwnerName(), account.getBalance());
        }
        System.out.println("Accounts created: " + BankAccount.getAccountCount());
        input.close();
    }

    private static BankAccount findAccount(BankAccount[] accounts, String number) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(number)) return account;
        }
        return null;
    }
    private static int readIntInRange(Scanner s, String prompt, int min, int max) {
        int value;
        do { value = readNonNegativeInt(s, prompt); } while (value < min || value > max);
        return value;
    }
    private static int readNonNegativeInt(Scanner s, String prompt) {
        int value;
        do { System.out.print(prompt); while (!s.hasNextInt()) { s.nextLine(); System.out.print(prompt); } value = s.nextInt(); s.nextLine(); } while (value < 0);
        return value;
    }
    private static double readNonNegativeDouble(Scanner s, String prompt) {
        double value;
        do { value = readDouble(s, prompt); } while (value < 0);
        return value;
    }
    private static double readDouble(Scanner s, String prompt) {
        System.out.print(prompt);
        while (!s.hasNextDouble()) { s.nextLine(); System.out.print(prompt); }
        double value = s.nextDouble(); s.nextLine(); return value;
    }
}

class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;
    private static int accountCount;

    public BankAccount(String accountNumber, String ownerName, double openingBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
        accountCount++;
    }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public double getBalance() { return balance; }
    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        balance += amount; return true;
    }
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount; return true;
    }
    public static int getAccountCount() { return accountCount; }
}


