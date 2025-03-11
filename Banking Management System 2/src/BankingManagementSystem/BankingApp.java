package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/bankingsystem";
    private static final String username = "root";
    private static final String password = "hardik8898";

    public static void main(String args[]) {
        try {
            // Load JDBC driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("JDBC Driver Loaded Successfully.");
            } catch (ClassNotFoundException e) {
                System.out.println("Error: MySQL JDBC Driver not found!");
                e.printStackTrace();
                return;
            }

            // Connect to Database
            Connection connection;
            try {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database Connection Established.");
            } catch (SQLException e) {
                System.out.println("Database Connection Failed!");
                e.printStackTrace();
                return;
            }

            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_number;

            while (true) {
                System.out.println("\n*** WELCOME TO BANKING SYSTEM ***");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume invalid input
                    continue;
                }

                int choice1 = scanner.nextInt();
                scanner.nextLine(); // Fix nextInt() issue

                switch (choice1) {
                    case 1:
                        System.out.println("Registering a new user...");
                        user.register();
                        break;

                    case 2:
                        System.out.println("Attempting Login...");
                        email = user.Login(); // Fixed method name

                        if (email != null) {
                            System.out.println("User Logged In Successfully!");

                            if (!accounts.account_exist(email)) {
                                System.out.println("No bank account found. Choose an option:");
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");

                                if (!scanner.hasNextInt()) {
                                    System.out.println("Invalid input. Please enter a number.");
                                    scanner.next(); // Consume invalid input
                                    break;
                                }

                                int accountChoice = scanner.nextInt();
                                // Fix nextInt() issue

                                if (accountChoice == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully!");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }

                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;

                            while (choice2 != 5) {
                                System.out.println("\nBanking Options:");
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter Your Choice: ");

                                if (!scanner.hasNextInt()) {
                                    System.out.println("Invalid input. Please enter a number.");
                                    scanner.next(); // Consume invalid input
                                    continue;
                                }

                                choice2 = scanner.nextInt();
                                // Fix nextInt() issue

                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        System.out.println("Logging Out...");
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Please enter a valid option.");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Login Failed: Incorrect Email or Password.");
                        }
                        break;

                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!");
                        System.out.println("Exiting System...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred!");
            e.printStackTrace();
        }
    }
}
