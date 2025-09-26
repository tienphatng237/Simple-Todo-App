package com.example.todoapp.dataaccess;

import androidx.room.*;
import com.example.todoapp.model.Task;
import java.util.List;

@Dao
public interface TaskDataAccess {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Task> getAllTasks();
}
