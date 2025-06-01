package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    // Constructor with all fields
    public User(int id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Constructor without email (if needed)
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = null; // or set a default email if necessary
    }

    // Getters and setters for all fields
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    // Setters if needed
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
}
