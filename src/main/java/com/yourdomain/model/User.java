package com.yourdomain.model;

/** A registered account (any role). */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String fullName;
    private String role;    // ADMIN, CASHIER, CLERK, CUSTOMER
    private String status;  // PENDING, APPROVED
    private String dateCreated;

    public User(int id, String username, String passwordHash, String fullName,
                String role, String status, String dateCreated) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.dateCreated = dateCreated;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getDateCreated() { return dateCreated; }
}
