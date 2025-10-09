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

import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddTaskActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription, edtDeadline;
    private Button btnSelectImage, btnSave;
    private ImageView imgPreview;
    private Uri selectedImageUri;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtDeadline = findViewById(R.id.edtDeadline);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSave);
        imgPreview = findViewById(R.id.imgPreview);

        db = AppDatabase.getInstance(this);

        edtDeadline.setFocusable(false);
        edtDeadline.setOnClickListener(v -> showDatePickerDialog());

        btnSelectImage.setOnClickListener(v -> pickImage());
        btnSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, y, m, d) -> edtDeadline.setText(
                        String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d)
                ),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imgPreview.setImageURI(uri);
                }
            });

    private void pickImage() {
        imagePicker.launch("image/*");
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            String fileName = "task_" + System.currentTimeMillis() + ".jpg";
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

    private void saveTask() {
        String title = edtTitle.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        String deadline = edtDeadline.getText().toString().trim();

        if (title.isEmpty() || deadline.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        String imagePath = (selectedImageUri != null)
                ? saveImageToInternalStorage(selectedImageUri)
                : null;

        Task task = new Task(title, desc, deadline, false, username, imagePath);

        Executors.newSingleThreadExecutor().execute(() -> {
            db.taskDao().insert(task);
            runOnUiThread(() -> {
                Toast.makeText(this, "Đã thêm công việc!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
