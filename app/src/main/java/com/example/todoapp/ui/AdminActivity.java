package com.example.todoapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.todoapp.R;
import com.example.todoapp.util.UserManager;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ListView listUsers;
    private Switch switchTheme;
    private Button btnLogout;
    private SharedPreferences prefs;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userManager = new UserManager(this);

        tvTitle = findViewById(R.id.tvTitle);
        listUsers = findViewById(R.id.listUsers);
        switchTheme = findViewById(R.id.switch_admin_theme);
        btnLogout = findViewById(R.id.btnLogout);

        // Áp dụng theme hiện tại
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        switchTheme.setChecked(darkMode);
        switchTheme.setOnCheckedChangeListener((b, checked) -> {
            prefs.edit().putBoolean("dark_mode", checked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate();
        });

        // Hiển thị danh sách user
        List<String> users = userManager.getAllUsers();
        if (users.isEmpty()) {
            Toast.makeText(this, "Không có người dùng nào.", Toast.LENGTH_SHORT).show();
        } else {
            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, users);
            listUsers.setAdapter(adapter);
        }

        // Nút logout riêng (chuẩn UX)
        btnLogout.setOnClickListener(v -> {
            prefs.edit().remove("username").apply();
            Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
