package com.example.todoapp.data;

import android.content.Context;

import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TaskRepository
 * - Bọc truy cập Room (TaskDao) và bảo đảm chạy trên background thread.
 * - Giai đoạn 1 chỉ cung cấp insert/update/delete để giảm diff.
 * - Các truy vấn lọc/danh sách vẫn giữ nguyên ở FragmentTaskList (phase sau sẽ chuyển dần).
 */
public class TaskRepository {
    private final TaskDao taskDao;
    private final ExecutorService executor;

    public TaskRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.taskDao = db.taskDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void insert(Task task, Runnable onDone) {
        executor.execute(() -> {
            taskDao.insert(task);
            if (onDone != null) onDone.run();
        });
    }

    public void update(Task task, Runnable onDone) {
        executor.execute(() -> {
            taskDao.update(task);
            if (onDone != null) onDone.run();
        });
    }

    public void delete(Task task, Runnable onDone) {
        executor.execute(() -> {
            taskDao.delete(task);
            if (onDone != null) onDone.run();
        });
    }
}
