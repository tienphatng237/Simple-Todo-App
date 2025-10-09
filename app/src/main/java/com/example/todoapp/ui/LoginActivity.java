package com.example.todoapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.todoapp.R;
import com.example.todoapp.util.UserManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private Switch switchTheme;
    private UserManager userManager;

    // 🟩 SharedPreferences: dùng để lưu dữ liệu dạng key–value (ví dụ: username, dark mode)
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo SharedPreferences và đọc trạng thái Dark Mode đã lưu
        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);

        // Áp dụng theme tương ứng với chế độ đã lưu
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        setContentView(R.layout.activity_login);

        // Ánh xạ view
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        switchTheme = findViewById(R.id.switch_theme);
        userManager = new UserManager(this);

        // Hiển thị trạng thái Switch theo giá trị darkMode đã lưu
        switchTheme.setChecked(darkMode);

        // Khi người dùng bật/tắt Dark Mode → ghi lại vào SharedPreferences
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply(); // ghi key–value
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // Sự kiện nút Login và Register
        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    // Xử lý đăng nhập
    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userManager.loginUser(username, password)) {
            // Lưu username đã đăng nhập vào SharedPreferences
            prefs.edit().putString("username", username).apply();

            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            if (username.equals("admin")) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        } else {
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }
}
