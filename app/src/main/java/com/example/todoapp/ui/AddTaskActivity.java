package com.example.todoapp.ui;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.R;
import com.example.todoapp.model.Task;
import com.example.todoapp.viewmodel.TaskViewModel;
import com.example.todoapp.util.FileUtil;

import java.util.concurrent.Executors;

/**
 * AddTaskActivity
 * - Giai đoạn 1: chuyển thao tác insert sang TaskViewModel.
 * - Các phần chọn ảnh / deadline / username giữ nguyên logic cũ.
 */
public class AddTaskActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription, edtDeadline;
    private ImageView imgPreview;
    private Button btnSave, btnSelectImage, btnPickDate;
    private Uri selectedImageUri;
    private String username;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // bind view
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtDeadline = findViewById(R.id.edtDeadline);
        imgPreview = findViewById(R.id.imgPreview);
        btnSave = findViewById(R.id.btnSave);
        btnSelectImage = findViewById(R.id.btnSelectImage);


        // lấy username từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = prefs.getString("username", "");

        // ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String desc = edtDescription.getText().toString().trim();
            String deadline = edtDeadline.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu ảnh vào internal storage nếu có (giữ nguyên util hiện có)
            String imagePath = (selectedImageUri != null)
                    ? FileUtil.saveImageToInternalStorage(this, selectedImageUri)
                    : null;

            Task task = new Task(title, desc, deadline, false, username, imagePath);

            // Ghi DB qua ViewModel (không gọi db.taskDao().insert trực tiếp)
            taskViewModel.insert(task, () -> runOnUiThread(() -> {
                Toast.makeText(this, "Đã thêm công việc!", Toast.LENGTH_SHORT).show();
                finish();
            }));
        });
    }
}
