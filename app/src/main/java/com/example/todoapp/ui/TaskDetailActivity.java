package com.example.todoapp.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

public class TaskDetailActivity extends AppCompatActivity {

    private TextView textTitle, textDescription, textDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        textTitle = findViewById(R.id.textTitle);
        textDescription = findViewById(R.id.textDescription);
        textDeadline = findViewById(R.id.textDeadline);

        int taskId = getIntent().getIntExtra("task_id", -1);

        if (taskId != -1) {
            new Thread(() -> {
                Task task = AppDatabase.getInstance(this).taskDao().getTaskById(taskId);
                if (task != null) {
                    runOnUiThread(() -> {
                        textTitle.setText(task.getTitle());
                        textDescription.setText(task.getDescription());
                        textDeadline.setText("⏰ Deadline: " + task.getDeadline());
                    });
                }
            }).start();
        }
    }
}
