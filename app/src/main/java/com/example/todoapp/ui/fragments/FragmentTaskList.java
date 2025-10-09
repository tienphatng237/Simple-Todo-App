package com.example.todoapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.adapter.TaskAdapter;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;
import com.example.todoapp.ui.AddTaskActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FragmentTaskList extends Fragment {

    private RecyclerView recyclerView;
    private Spinner spinnerFilter;
    private TaskAdapter adapter;
    private AppDatabase db;
    private SharedPreferences prefs;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        spinnerFilter = view.findViewById(R.id.spinnerFilter);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_task);

        db = AppDatabase.getInstance(requireContext());
        prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        username = prefs.getString("username", "");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(getContext(), new ArrayList<>(), db);
        recyclerView.setAdapter(adapter);

        setupSpinner();
        loadTasks("All"); // mặc định hiển thị tất cả

        // FAB: mở màn thêm task
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void setupSpinner() {
        String[] options = {"All", "Active", "Completed"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTasks(options[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadTasks(String filter) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Task> tasks;
            switch (filter) {
                case "Active":
                    tasks = db.taskDao().getTasksByStatus(username, false);
                    break;
                case "Completed":
                    tasks = db.taskDao().getTasksByStatus(username, true);
                    break;
                default:
                    tasks = db.taskDao().getTasksByUser(username);
                    break;
            }
            requireActivity().runOnUiThread(() -> adapter.setTasks(tasks));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String current = (spinnerFilter.getSelectedItem() instanceof String)
                ? (String) spinnerFilter.getSelectedItem() : "All";
        loadTasks(current);
    }
}
