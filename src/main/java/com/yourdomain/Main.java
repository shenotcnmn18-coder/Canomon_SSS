package com.yourdomain;

import com.yourdomain.dao.DatabaseInitializer;
import com.yourdomain.dao.UserDao;
import com.yourdomain.ui.Authentication;

import java.util.Scanner;

/** Startup entry point only: init DB -> create shared DAO/Scanner -> hand off to Authentication. */
public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing database...");
        DatabaseInitializer.initialize();
        System.out.println("Database ready.");

        UserDao userDao = new UserDao();
        try (Scanner scanner = new Scanner(System.in)) {
            new Authentication(userDao, scanner).start();
        }
    }
}
