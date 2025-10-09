package com.example.todoapp.util;

import android.content.Context;

import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtil {

    private static final String EXPORT_FILE_NAME = "tasks.json";

    // Ghi text vào internal storage
    public static String saveText(Context context, String fileName, String content) throws Exception {
        File out = new File(context.getFilesDir(), fileName);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8))) {
            bw.write(content);
        }
        return out.getAbsolutePath();
    }

    // Đọc text từ internal storage
    public static String readText(Context context, String fileName) throws Exception {
        File in = new File(context.getFilesDir(), fileName);
        if (!in.exists()) return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(in), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
        }
        return sb.toString();
    }

    // Export toàn bộ Task trong DB thành JSON -> /files/tasks.json
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
            arr.put(o);
        }
        JSONObject root = new JSONObject();
        root.put("count", tasks.size());
        root.put("tasks", arr);

        return saveText(context, EXPORT_FILE_NAME, root.toString(2));
    }
}
