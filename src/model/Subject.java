package model;

public class Subject {
    private int id;
    private String name;

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }

    // Setters (if needed)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}