package com.example.todoapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserManager {

    private static final String PREFS_NAME = "user_data";
    private SharedPreferences prefs;

    public UserManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Tạo admin mặc định (nếu chưa có)
        if (!prefs.contains("admin_password")) {
            prefs.edit().putString("admin_password", "123456").apply();
        }
    }

    public boolean registerUser(String username, String password) {
        if (prefs.contains(username + "_password")) return false;
        prefs.edit().putString(username + "_password", password).apply();
        return true;
    }

    public boolean loginUser(String username, String password) {
        String savedPass = prefs.getString(username + "_password", null);

        // Nếu là admin
        if (username.equals("admin")) {
            String adminPass = prefs.getString("admin_password", "123456");
            return password.equals(adminPass);
        }

        return savedPass != null && savedPass.equals(password);
    }

    public List<String> getAllUsers() {
        Map<String, ?> all = prefs.getAll();
        List<String> users = new ArrayList<>();
        for (String key : all.keySet()) {
            if (key.endsWith("_password")) {
                users.add(key.replace("_password", ""));
            }
        }
        return users;
    }
}
