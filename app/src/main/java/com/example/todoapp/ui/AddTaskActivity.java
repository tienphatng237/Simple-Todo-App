package com.example.todoapp.ui;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText etTitle = findViewById(R.id.et_title);
        EditText etDesc = findViewById(R.id.et_description);
        EditText etDeadline = findViewById(R.id.et_deadline);
        Button btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String desc = etDesc.getText().toString();
            String deadline = etDeadline.getText().toString();

            // Create a new Task (completed = false by default)
            Task task = new Task(title, desc, deadline, false);

            // Save to the database
            AppDatabase.getInstance(this).taskDataAccess().insert(task);

            // Return to the previous screen
            finish();
        });
    }
}
