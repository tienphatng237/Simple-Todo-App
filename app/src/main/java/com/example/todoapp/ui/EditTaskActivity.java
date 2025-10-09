package com.example.todoapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDeadline;
    private Button btnSave, btnSelectImage;
    private ImageView imgPreview;
    private AppDatabase db;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDeadline = findViewById(R.id.et_deadline);
        btnSave = findViewById(R.id.btn_save);
        btnSelectImage = findViewById(R.id.btn_select_image);
        imgPreview = findViewById(R.id.img_preview);

        db = AppDatabase.getInstance(this);
        int taskId = getIntent().getIntExtra("task_id", -1);

        if (taskId != -1) {
            Executors.newSingleThreadExecutor().execute(() -> {
                currentTask = db.taskDao().getTaskById(taskId);
                runOnUiThread(() -> {
                    if (currentTask != null) {
                        etTitle.setText(currentTask.getTitle());
                        etDescription.setText(currentTask.getDescription());
                        etDeadline.setText(currentTask.getDeadline());
                        if (currentTask.getImagePath() != null)
                            imgPreview.setImageURI(Uri.fromFile(new File(currentTask.getImagePath())));
                    }
                });
            });
        }

        btnSelectImage.setOnClickListener(v -> imagePicker.launch("image/*"));
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && currentTask != null) {
                    String savedPath = saveImageToInternalStorage(uri);
                    currentTask.setImagePath(savedPath);
                    imgPreview.setImageURI(Uri.fromFile(new File(savedPath)));
                }
            });

    private String saveImageToInternalStorage(Uri uri) {
        try {
            String fileName = "task_edit_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getFilesDir(), fileName);
            try (InputStream in = getContentResolver().openInputStream(uri);
                 OutputStream out = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) out.write(buffer, 0, len);
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveChanges() {
        if (currentTask == null) return;

        currentTask.setTitle(etTitle.getText().toString());
        currentTask.setDescription(etDescription.getText().toString());
        currentTask.setDeadline(etDeadline.getText().toString());

        Executors.newSingleThreadExecutor().execute(() -> {
            db.taskDao().update(currentTask);
            runOnUiThread(() -> {
                Toast.makeText(this, "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
