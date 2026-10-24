# Sari-Sari Store Inventory and Sales Management System

Console (CLI) app — Java 17, SQLite (sqlite-jdbc), Maven.

## Run
    mvn compile exec:java        # or run com.yourdomain.Main from your IDE

First launch creates `sari_sari_store.db`. The **first account registered is always the
Store Owner / Administrator** (auto-APPROVED, no role choice); every later account is `PENDING`.

## Flow
Main -> DatabaseInitializer.initialize() -> new UserDao() + Scanner -> Authentication.start()
Authentication routes by role to AdminMenu / CashierMenu / ClerkMenu / CustomerMenu.
New registrations are `PENDING` until an admin approves them (Admin Menu > Approve User Account).

## Structure
    com.yourdomain
    ├── Main.java
    ├── dao   DatabaseConnection (PRAGMA foreign_keys = ON), DatabaseInitializer, UserDao
    ├── model User
    ├── ui    Authentication, AdminMenu, CashierMenu, ClerkMenu, CustomerMenu
    └── util  PasswordUtil
