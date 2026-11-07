package com.yourdomain.ui;

import com.yourdomain.dao.UserDao;
import com.yourdomain.model.User;

import java.util.List;
import java.util.Set;
import java.util.Scanner;

/** Store Owner / Administrator menu. */
public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    private static final Set<String> VALID_ROLES = Set.of("ADMIN", "CASHIER", "CLERK", "CUSTOMER");
    private static final Set<String> VALID_STATUSES = Set.of("APPROVED", "PENDING");

    public AdminMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show(User admin) {
        while (true) {
            System.out.println("\n===== ADMIN MENU (" + admin.getFullName() + ") =====");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Update User Role / Status");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> viewPendingUsers();
                case "3" -> approveUser();
                case "4" -> updateUserRoleAndStatus(admin);
                case "0" -> { System.out.println("Logged out."); return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n--- All System Users ---");
        printUserTable(userDao.getAllUsers());
    }

    private void viewPendingUsers() {
        System.out.println("\n--- Pending Registrations ---");
        printUserTable(userDao.getPendingUsers());
    }

    private void approveUser() {
        List<User> pending = userDao.getPendingUsers();
        System.out.println("\n--- Approve User Account ---");
        printUserTable(pending);
        if (pending.isEmpty()) return;

        while (true) {
            System.out.print("Enter Target User ID to approve (0 to cancel): ");
            String input = scanner.nextLine().trim();
            int id;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }
            if (id == 0) {
                System.out.println("Approval cancelled.");
                return;
            }
            boolean inPending = pending.stream().anyMatch(u -> u.getId() == id);
            if (!inPending) {
                System.out.println("ID " + id + " is not in the pending list. Try again.");
                continue;
            }
            if (userDao.approveUser(id)) {
                System.out.println("User ID " + id + " has been APPROVED.");
            } else {
                System.out.println("Approval failed. Please try again.");
            }
            return;
        }
    }

    private void updateUserRoleAndStatus(User currentAdmin) {
        List<User> users = userDao.getAllUsers();
        System.out.println("\n--- Update User Role / Status ---");
        printUserTable(users);
        if (users.isEmpty()) return;

        int targetUserId;
        while (true) {
            targetUserId = readIntInput("Enter Target User ID (0 to cancel): ");
            if (targetUserId == 0) { cancelled(); return; }
            final int id = targetUserId;
            if (id == currentAdmin.getId()) {
                System.out.println("WARNING: Modifying the currently active admin account is restricted.");
                continue;
            }
            if (users.stream().noneMatch(u -> u.getId() == id)) {
                System.out.println("ID " + id + " does not exist. Try again.");
                continue;
            }
            break;
        }

        String newRole = readValidRole();
        if (newRole == null) { cancelled(); return; }
        String newStatus = readValidStatus();
        if (newStatus == null) { cancelled(); return; }

        if (userDao.updateUserRoleAndStatus(targetUserId, newRole, newStatus)) {
            System.out.println("User ID " + targetUserId + " updated: role=" + newRole
                    + ", status=" + newStatus + ".");
        } else {
            System.out.println("Update failed. Please try again.");
        }
    }

    private void cancelled() {
        System.out.println("Update cancelled.");
    }

    /** Re-prompts until the user types a whole number; never throws on bad input. */
    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /** Re-prompts until a valid role is entered. Returns null if the user enters 0 (cancel). */
    private String readValidRole() {
        while (true) {
            System.out.print("Enter new role " + VALID_ROLES.stream().sorted().toList()
                    + " (0 to cancel): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null;
            if (VALID_ROLES.contains(input)) return input;
            System.out.println("Invalid role. Please try again.");
        }
    }

    /** Re-prompts until APPROVED or PENDING is entered. Returns null if the user enters 0 (cancel). */
    private String readValidStatus() {
        while (true) {
            System.out.print("Enter new status [APPROVED, PENDING] (0 to cancel): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null;
            if (VALID_STATUSES.contains(input)) return input;
            System.out.println("Invalid status. Please try again.");
        }
    }

    /** Renders any list of users as an aligned, bordered table. */
    private void printUserTable(List<User> users) {
        String rowFmt = "| %-5s | %-15s | %-12s | %-10s |%n";
        String border = "+" + "-".repeat(7) + "+" + "-".repeat(17) + "+" + "-".repeat(14) + "+" + "-".repeat(12) + "+";

        System.out.println(border);
        System.out.printf(rowFmt, "ID", "Username", "Role", "Status");
        System.out.println(border);
        if (users.isEmpty()) {
            System.out.printf("| %-53s |%n", "No records found.");
        } else {
            for (User u : users) {
                System.out.printf(rowFmt, u.getId(), truncate(u.getUsername(), 15),
                        u.getRole(), u.getStatus());
            }
        }
        System.out.println(border);
        System.out.println("Total: " + users.size());
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "~";
    }
}