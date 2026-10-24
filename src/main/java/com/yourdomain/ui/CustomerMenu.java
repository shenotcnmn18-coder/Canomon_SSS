package com.yourdomain.ui;

import com.yourdomain.dao.UserDao;
import com.yourdomain.model.User;

import java.util.Scanner;

/** Customer menu (View your credit balance and purchase history). */
public class CustomerMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public CustomerMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show(User user) {
        while (true) {
            System.out.println("\n===== CUSTOMER MENU (" + user.getFullName() + ") =====");
            System.out.println("1. (placeholder) Coming soon: credit balance and purchase history.");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) { System.out.println("Logged out."); return; }
            System.out.println(choice.equals("1") ? "Coming soon: credit balance and purchase history." : "Invalid choice. Try again.");
        }
    }
}
