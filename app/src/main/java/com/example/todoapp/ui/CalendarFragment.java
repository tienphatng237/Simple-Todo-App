package com.example.todoapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.adapter.TaskAdapter;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private TextView textSelectedDate;
    private RecyclerView recyclerTasks;
    private TaskAdapter taskAdapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        textSelectedDate = view.findViewById(R.id.textSelectedDate);
        recyclerTasks = view.findViewById(R.id.recyclerTasksForDate);

        db = AppDatabase.getInstance(requireContext());
        taskAdapter = new TaskAdapter(requireContext(), new ArrayList<>(), db);

        recyclerTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerTasks.setAdapter(taskAdapter);

        // Hiển thị task của ngày hôm nay
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        textSelectedDate.setText("Công việc ngày: " + today);
        loadTasksForDate(today);

        // Khi chọn ngày mới
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            textSelectedDate.setText("Công việc ngày: " + selectedDate);
            loadTasksForDate(selectedDate);
        });

        return view;
    }

    private void loadTasksForDate(String date) {
        new Thread(() -> {
            // Lấy user hiện tại từ SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String username = prefs.getString("username", "guest");

            // Lọc task theo ngày & user
            List<Task> tasks = db.taskDao().getTasksByDeadline(date, username);

            requireActivity().runOnUiThread(() -> taskAdapter.setTasks(tasks));
        }).start();
    }
}
