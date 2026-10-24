package com.yourdomain.ui;

import com.yourdomain.dao.UserDao;
import com.yourdomain.model.User;

import java.util.Scanner;

/** Cashier / Staff menu (Record sales, search products, view stock and customer credit balances). */
public class CashierMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public CashierMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show(User user) {
        while (true) {
            System.out.println("\n===== CASHIER / STAFF MENU (" + user.getFullName() + ") =====");
            System.out.println("1. (placeholder) Coming soon: sales, product search, stock levels, credit balances.");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) { System.out.println("Logged out."); return; }
            System.out.println(choice.equals("1") ? "Coming soon: sales, product search, stock levels, credit balances." : "Invalid choice. Try again.");
        }
    }
}
