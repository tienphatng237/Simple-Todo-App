package com.example.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.todoapp.data.TaskRepository;
import com.example.todoapp.model.Task;

/**
 * TaskViewModel
 * - Cầu nối UI <-> Repository.
 * - Giữ API tối giản (insert/update/delete) để không phá flow hiện tại.
 */
public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public void insert(Task task, Runnable onDone) {
        repository.insert(task, onDone);
    }

    public void update(Task task, Runnable onDone) {
        repository.update(task, onDone);
    }

    public void delete(Task task, Runnable onDone) {
        repository.delete(task, onDone);
    }
}
