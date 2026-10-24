package com.yourdomain.ui;

import com.yourdomain.dao.UserDao;
import com.yourdomain.model.User;

import java.util.Scanner;

/** Inventory Clerk menu (Add products, update stock on delivery, view low-stock report). */
public class ClerkMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public ClerkMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show(User user) {
        while (true) {
            System.out.println("\n===== INVENTORY CLERK MENU (" + user.getFullName() + ") =====");
            System.out.println("1. (placeholder) Coming soon: add products, restock, low-stock report.");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) { System.out.println("Logged out."); return; }
            System.out.println(choice.equals("1") ? "Coming soon: add products, restock, low-stock report." : "Invalid choice. Try again.");
        }
    }
}
