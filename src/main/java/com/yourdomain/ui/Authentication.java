package com.yourdomain.ui;

import com.yourdomain.dao.UserDao;
import com.yourdomain.model.User;

import java.util.Scanner;

/** Handles login, registration and role-based routing. */
public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== SARI-SARI STORE INVENTORY & SALES SYSTEM ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> login();
                case "2" -> register();
                case "0" -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userDao.authenticate(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }
        if (!"APPROVED".equals(user.getStatus())) {
            System.out.println("Your account is " + user.getStatus() + ". Please wait for the administrator's approval.");
            return;
        }
        System.out.println("Welcome, " + user.getFullName() + "! (" + user.getRole() + ")");
        routeByRole(user);
    }

    private void routeByRole(User user) {
        switch (user.getRole()) {
            case "ADMIN"    -> new AdminMenu(userDao, scanner).show(user);
            case "CASHIER"  -> new CashierMenu(userDao, scanner).show(user);
            case "CLERK"    -> new ClerkMenu(userDao, scanner).show(user);
            case "CUSTOMER" -> new CustomerMenu(userDao, scanner).show(user);
            default -> System.out.println("Unknown role: " + user.getRole());
        }
    }

    private void register() {
        System.out.println("\n--- Register New Account ---");
        String fullName = prompt("Full name: ");
        if (fullName.isEmpty()) { System.out.println("Full name is required."); return; }

        String username = prompt("Username: ");
        if (username.isEmpty()) { System.out.println("Username is required."); return; }
        if (userDao.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }

        String password = prompt("Password (min 6 chars): ");
        if (password.length() < 6) { System.out.println("Password too short."); return; }

        boolean first = userDao.isFirstAccount();
        String role;
        if (first) {
            role = "ADMIN";
            System.out.println("No accounts exist yet: this first account will be the Store Owner / Administrator.");
        } else {
            role = chooseRole();
            if (role == null) { System.out.println("Registration cancelled."); return; }
        }

        if (userDao.registerUser(username, password, fullName, role)) {
            if (first) {
                System.out.println("Registration successful! You are the Store Owner / Administrator (APPROVED). You can log in now.");
            } else {
                System.out.println("Registration successful! Status: PENDING. An administrator must approve your account before you can log in.");
            }
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private String chooseRole() {
        while (true) {
            System.out.println("Select role:");
            System.out.println("1. Store Owner / Administrator");
            System.out.println("2. Cashier / Staff");
            System.out.println("3. Inventory Clerk");
            System.out.println("4. Customer");
            System.out.println("0. Cancel");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1": return "ADMIN";
                case "2": return "CASHIER";
                case "3": return "CLERK";
                case "4": return "CUSTOMER";
                case "0": return null;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }
}
