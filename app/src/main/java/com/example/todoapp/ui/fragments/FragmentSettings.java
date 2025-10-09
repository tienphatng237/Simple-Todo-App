package com.example.todoapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todoapp.R;
import com.example.todoapp.ui.LoginActivity;
import com.example.todoapp.util.FileUtil;
import com.example.todoapp.util.ThemeManager;

public class FragmentSettings extends Fragment {

    private TextView tvUsername;
    private Switch switchTheme;
    private Button btnExport, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Ánh xạ view
        tvUsername = view.findViewById(R.id.tvUsername);
        switchTheme = view.findViewById(R.id.switchTheme);
        btnExport = view.findViewById(R.id.btnExport);
        btnLogout = view.findViewById(R.id.btnLogout);

        // SharedPreferences: Lấy username đã lưu khi đăng nhập
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Guest");
        tvUsername.setText("Xin chào, " + username);

        // ThemeManager: Kiểm tra trạng thái dark mode đã lưu (SharedPreferences)
        switchTheme.setChecked(ThemeManager.isDarkMode(requireContext()));
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 🟩 Ghi lại giá trị dark mode vào SharedPreferences
            ThemeManager.setDarkMode(requireContext(), isChecked);
            requireActivity().recreate();
        });

        // File Storage: Xuất toàn bộ dữ liệu Task ra file JSON nội bộ
        btnExport.setOnClickListener(v -> {
            try {
                String path = FileUtil.exportAllTasksToJson(requireContext());
                Toast.makeText(requireContext(), "Đã xuất JSON: " + path, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Lỗi khi xuất JSON", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout: Xóa dữ liệu người dùng đã lưu trong SharedPreferences
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
