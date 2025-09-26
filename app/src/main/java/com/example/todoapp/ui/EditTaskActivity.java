package com.example.todoapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import java.util.Calendar;


public class EditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDesc, etDeadline;
    private Button btnUpdate;
    private Task task;  // Task being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task); // reuse add_task layout

        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
        etDeadline = findViewById(R.id.et_deadline);
        btnUpdate = findViewById(R.id.btn_save);
        btnUpdate.setText("Update Task");

        etDeadline.setFocusable(false);
        etDeadline.setOnClickListener(v -> showDatePicker());


        // Get taskId from Intent
        int taskId = getIntent().getIntExtra("taskId", -1);

        if (taskId != -1) {
            // Get Task from DB
            task = AppDatabase.getInstance(this).taskDataAccess().getTaskById(taskId);

            if (task != null) {
                etTitle.setText(task.title);
                etDesc.setText(task.description);
                etDeadline.setText(task.deadline);
            } else {
                Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            Toast.makeText(this, "Invalid task", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Update task
        btnUpdate.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String deadline = etDeadline.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(deadline)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            task.title = title;
            task.description = desc;
            task.deadline = deadline;

            AppDatabase.getInstance(this).taskDataAccess().update(task);

            Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Format yyyy-MM-dd
                    String date = String.format("%04d-%02d-%02d", year1, (month1 + 1), dayOfMonth);
                    etDeadline.setText(date);
                }, year, month, day);

        // Do not allow selecting past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

}
