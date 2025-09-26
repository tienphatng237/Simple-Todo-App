package com.example.todoapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import java.util.regex.Pattern;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDesc, etDeadline;
    private Button btnSave;

    // Regex to validate the date format yyyy-mm-dd
    private static final Pattern DATE_PATTERN =
            Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
        etDeadline = findViewById(R.id.et_deadline);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();

        boolean valid = true;

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Title is required");
            valid = false;
        }
        if (TextUtils.isEmpty(desc)) {
            etDesc.setError("Description is required");
            valid = false;
        }
        if (TextUtils.isEmpty(deadline)) {
            etDeadline.setError("Deadline is required");
            valid = false;
        } else if (!DATE_PATTERN.matcher(deadline).matches()) {
            etDeadline.setError("Invalid format. Use yyyy-mm-dd");
            valid = false;
        }

        if (!valid) {
            Toast.makeText(this, "Please fix the errors", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new task (completed = false)
        Task task = new Task(title, desc, deadline, false);

        // Save to the database
        AppDatabase.getInstance(this).taskDataAccess().insert(task);

        Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
