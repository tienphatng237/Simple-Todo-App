package com.example.todoapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String deadline;
    private boolean completed;
    private String username;
    private String imagePath; // thêm ảnh

    public Task(String title, String description, String deadline,
                boolean completed, String username, String imagePath) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.username = username;
        this.imagePath = imagePath;
    }

    // --- Getter & Setter ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
