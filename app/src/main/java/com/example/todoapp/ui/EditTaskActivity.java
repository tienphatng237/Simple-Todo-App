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
import com.example.todoapp.util.FileUtil;
import com.example.todoapp.viewmodel.TaskViewModel;

/**
 * EditTaskActivity
 * -------------------------------
 * - Dùng để chỉnh sửa công việc.
 * - Liên kết với layout activity_edit_task.xml (đã kiểm tra ID).
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

        // Bind View
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDeadline = findViewById(R.id.et_deadline);
        imgPreview = findViewById(R.id.img_preview);
        btnSave = findViewById(R.id.btn_save);
        btnSelectImage = findViewById(R.id.btn_select_image);

        // ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Nhận Task từ Intent
        if (getIntent().hasExtra("task")) {
            currentTask = (Task) getIntent().getSerializableExtra("task");
            if (currentTask != null) {
                etTitle.setText(currentTask.getTitle());
                etDescription.setText(currentTask.getDescription());
                etDeadline.setText(currentTask.getDeadline());

                if (currentTask.getImagePath() != null) {
                    FileUtil.loadImageInto(imgPreview, currentTask.getImagePath());
                }
            }
        }

        // Sự kiện chọn ảnh
        btnSelectImage.setOnClickListener(v -> {
            // TODO: Giai đoạn 5 sẽ thay bằng SAF
            Toast.makeText(this, "Chức năng chọn ảnh sẽ thêm ở giai đoạn sau", Toast.LENGTH_SHORT).show();
        });

        // Sự kiện lưu thay đổi
        btnSave.setOnClickListener(v -> {
            if (currentTask == null) {
                Toast.makeText(this, "Không tìm thấy công việc để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }

            currentTask.setTitle(etTitle.getText().toString().trim());
            currentTask.setDescription(etDescription.getText().toString().trim());
            currentTask.setDeadline(etDeadline.getText().toString().trim());

            // Giai đoạn sau sẽ cập nhật lại imagePath nếu chọn ảnh mới
            taskViewModel.update(currentTask, () -> runOnUiThread(() -> {
                Toast.makeText(this, "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
                finish();
            }));
        });
    }
}
