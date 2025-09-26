package com.example.todoapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public String deadline;
    public boolean completed;

    public Task(String title, String description, String deadline, boolean completed) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
    }
}
