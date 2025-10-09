package com.example.todoapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks WHERE username = :username ORDER BY id DESC")
    List<Task> getTasksByUser(String username);

    @Query("SELECT * FROM tasks WHERE deadline = :date AND username = :username ORDER BY id DESC")
    List<Task> getTasksByDeadline(String date, String username);

    @Query("SELECT * FROM tasks WHERE username = :username AND completed = :isCompleted ORDER BY id DESC")
    List<Task> getTasksByStatus(String username, boolean isCompleted);

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    Task getTaskById(int taskId);

    // Thêm mới: trả về toàn bộ task (cho export JSON)
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Task> getAllTasks();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
