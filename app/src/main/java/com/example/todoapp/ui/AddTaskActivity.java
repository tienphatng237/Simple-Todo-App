package com.example.todoapp.ui;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import com.example.todoapp.R;
import com.example.todoapp.model.Task;
import com.example.todoapp.viewmodel.TaskViewModel;

import java.util.Calendar;
import java.util.concurrent.Executors;

/**
 * AddTaskActivity
 * - Giai đoạn 1: chuyển thao tác insert sang TaskViewModel.
 * - Các phần chọn ảnh / deadline / username giữ nguyên logic cũ.
 */
public class AddTaskActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription, edtDeadline;
    private ImageView imgPreview;
    private Button btnSave, btnSelectImage;
    private Uri selectedImageUri;
    private String username;
    private TaskViewModel taskViewModel;
    // --- ActivityResultLauncher cho chọn ảnh ---
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // --- Ánh xạ view ---
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtDeadline = findViewById(R.id.edtDeadline);
        imgPreview = findViewById(R.id.imgPreview);
        btnSave = findViewById(R.id.btnSave);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        // --- Lấy username từ SharedPreferences ---
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = prefs.getString("username", "");

        // --- ViewModel ---
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // --- Cấu hình chọn ảnh ---
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        imgPreview.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "Không chọn được ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // --- Gắn sự kiện chọn ngày cho edtDeadline ---
        edtDeadline.setFocusable(false);
        edtDeadline.setClickable(true);
        edtDeadline.setOnClickListener(v -> showDatePickerDialog());

        // --- Nút lưu task ---
        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String deadline = edtDeadline.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty() || deadline.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Task newTask = new Task(
                    title,
                    description,
                    deadline,
                    false,
                    username,
                    selectedImageUri != null ? selectedImageUri.toString() : null
            );

            // Gọi insert với callback
            taskViewModel.insert(newTask, () -> runOnUiThread(() -> {
                Toast.makeText(this, "Thêm công việc thành công", Toast.LENGTH_SHORT).show();
                finish();
            }));
        });
    }

    // --- Hiển thị DatePickerDialog ---
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%04d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);
                    edtDeadline.setText(selectedDate);
                },
                year, month, day
        );
        dialog.show();
    }
}