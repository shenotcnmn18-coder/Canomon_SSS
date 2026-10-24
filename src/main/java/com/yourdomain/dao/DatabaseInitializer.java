package com.yourdomain.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Creates every required table (if missing) before the CLI menu starts. */
public final class DatabaseInitializer {
    private DatabaseInitializer() {}

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    username      TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    full_name     TEXT NOT NULL,
                    role          TEXT NOT NULL CHECK (role IN ('ADMIN','CASHIER','CLERK','CUSTOMER')),
                    status        TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','APPROVED')),
                    date_created  TEXT NOT NULL DEFAULT (datetime('now','localtime'))
                )""");

            st.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    product_id     INTEGER PRIMARY KEY AUTOINCREMENT,
                    product_name   TEXT NOT NULL,
                    category       TEXT,
                    unit_price     REAL NOT NULL CHECK (unit_price >= 0),
                    stock_quantity INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
                    reorder_level  INTEGER NOT NULL DEFAULT 0,
                    status         TEXT NOT NULL DEFAULT 'active' CHECK (status IN ('active','inactive'))
                )""");

            st.execute("""
                CREATE TABLE IF NOT EXISTS sales (
                    sale_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    sale_date    TEXT NOT NULL DEFAULT (datetime('now','localtime')),
                    user_id      INTEGER NOT NULL,
                    customer_id  INTEGER,
                    total_amount REAL NOT NULL DEFAULT 0,
                    payment_type TEXT NOT NULL CHECK (payment_type IN ('cash','credit')),
                    status       TEXT NOT NULL DEFAULT 'completed' CHECK (status IN ('completed','voided')),
                    FOREIGN KEY (user_id)     REFERENCES users(id),
                    FOREIGN KEY (customer_id) REFERENCES users(id)
                )""");

            st.execute("""
                CREATE TABLE IF NOT EXISTS sale_items (
                    sale_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sale_id      INTEGER NOT NULL,
                    product_id   INTEGER NOT NULL,
                    quantity     INTEGER NOT NULL CHECK (quantity > 0),
                    unit_price   REAL NOT NULL,
                    subtotal     REAL NOT NULL,
                    FOREIGN KEY (sale_id)    REFERENCES sales(sale_id),
                    FOREIGN KEY (product_id) REFERENCES products(product_id)
                )""");

            st.execute("""
                CREATE TABLE IF NOT EXISTS activity_logs (
                    log_id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id        INTEGER,
                    action         TEXT NOT NULL,
                    table_affected TEXT,
                    record_id      INTEGER,
                    old_value      TEXT,
                    new_value      TEXT,
                    log_timestamp  TEXT NOT NULL DEFAULT (datetime('now','localtime')),
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )""");

        } catch (SQLException e) {
            throw new IllegalStateException("Database initialization failed: " + e.getMessage(), e);
        }
    }
}
