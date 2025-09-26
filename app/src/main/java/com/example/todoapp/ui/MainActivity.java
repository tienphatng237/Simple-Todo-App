package com.example.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.adapter.TaskAdapter;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Call loadTasks() the first time
        loadTasks();

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTaskActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Each time returning to this screen, reload the list
        loadTasks();
    }

    private void loadTasks() {
        List<Task> tasks = AppDatabase.getInstance(this).taskDataAccess().getAllTasks();
        adapter = new TaskAdapter(this, tasks);
        recyclerView.setAdapter(adapter);
    }
}