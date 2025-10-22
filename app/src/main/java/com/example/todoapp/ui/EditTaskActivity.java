package com.example.todoapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.R;
import com.example.todoapp.model.Task;
import com.example.todoapp.viewmodel.TaskViewModel;

import java.util.concurrent.Executors;

/**
 * EditTaskActivity
 * -------------------------------
 * - Dùng để chỉnh sửa công việc.
 * - Hiển thị dữ liệu sẵn có và cho phép sửa lại.
 */
public class EditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDeadline;
    private Button btnSave, btnSelectImage;
    private ImageView imgPreview;
    private Task currentTask;
    private Uri selectedImageUri;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // --- Ánh xạ View ---
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDeadline = findViewById(R.id.et_deadline);
        imgPreview = findViewById(R.id.img_preview);
        btnSave = findViewById(R.id.btn_save);
        btnSelectImage = findViewById(R.id.btn_select_image);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // --- Nhận taskId từ Intent ---
        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId == -1) {
            Toast.makeText(this, "Không tìm thấy công việc!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Lấy Task từ DB ---
        Executors.newSingleThreadExecutor().execute(() -> {
            currentTask = taskViewModel.getTaskById(taskId);
            if (currentTask != null) {
                runOnUiThread(() -> {
                    etTitle.setText(currentTask.getTitle());
                    etDescription.setText(currentTask.getDescription());
                    etDeadline.setText(currentTask.getDeadline());

                    if (currentTask.getImagePath() != null && !currentTask.getImagePath().isEmpty()) {
                        try {
                            imgPreview.setImageURI(Uri.parse(currentTask.getImagePath()));
                        } catch (Exception e) {
                            imgPreview.setImageResource(R.drawable.ic_image_placeholder);
                        }
                    }
                });
            }
        });

        // --- Nút chọn ảnh (chưa thêm mới) ---
        btnSelectImage.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng chọn ảnh mới sẽ thêm ở giai đoạn sau", Toast.LENGTH_SHORT).show()
        );

        // --- Nút lưu thay đổi ---
        btnSave.setOnClickListener(v -> {
            if (currentTask == null) {
                Toast.makeText(this, "Không tìm thấy công việc để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }

            currentTask.setTitle(etTitle.getText().toString().trim());
            currentTask.setDescription(etDescription.getText().toString().trim());
            currentTask.setDeadline(etDeadline.getText().toString().trim());

            taskViewModel.update(currentTask, () -> runOnUiThread(() -> {
                Toast.makeText(this, "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
                finish();
            }));
        });
    }
}
