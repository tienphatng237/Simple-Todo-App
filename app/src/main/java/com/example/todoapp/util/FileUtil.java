package com.example.todoapp.util;

import android.content.Context;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * FileUtil
 * - Quản lý đọc/ghi file nội bộ và export dữ liệu Task thành JSON.
 */
public class FileUtil {

    private static final String EXPORT_FILE_NAME = "tasks.json";


    // Save text vào Internal Storage
    public static String saveText(Context context, String fileName, String content) throws Exception {
        File out = new File(context.getFilesDir(), fileName);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8))) {
            bw.write(content);
        }
        return out.getAbsolutePath();
    }

    // Đọc text từ Internal Storage
    public static String readText(Context context, String fileName) throws Exception {
        File in = new File(context.getFilesDir(), fileName);
        if (!in.exists()) return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(in), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    // Export toàn bộ Task từ DB thành JSON
    public static String exportAllTasksToJson(Context context) throws Exception {
        List<Task> tasks = AppDatabase.getInstance(context).taskDao().getAllTasks();

        JSONArray arr = new JSONArray();
        for (Task t : tasks) {
            JSONObject o = new JSONObject();
            o.put("id", t.getId());
            o.put("title", t.getTitle());
            o.put("description", t.getDescription());
            o.put("deadline", t.getDeadline());
            o.put("completed", t.isCompleted());
            o.put("username", t.getUsername());
            o.put("imagePath", t.getImagePath());
            arr.put(o);
        }

        JSONObject root = new JSONObject();
        root.put("count", tasks.size());
        root.put("tasks", arr);

        return saveText(context, EXPORT_FILE_NAME, root.toString(2));
    }

    // Lưu ảnh vào Internal Storage
    public static String saveImageToInternalStorage(Context context, Uri imageUri) {
        try {
            // Mở luồng đọc dữ liệu ảnh
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);

            // Tạo tên file duy nhất
            String fileName = "task_image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(context.getFilesDir(), fileName);

            // Ghi file vào bộ nhớ trong
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            // Trả về đường dẫn file
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Load image từ đường dẫn nội bộ vào ImageView.
    public static void loadImageInto(ImageView imageView, String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return;

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }
}
