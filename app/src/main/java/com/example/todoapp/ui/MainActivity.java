package com.example.todoapp.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.todoapp.R;
import com.example.todoapp.ui.fragments.FragmentCalendar;
import com.example.todoapp.ui.fragments.FragmentSettings;
import com.example.todoapp.ui.fragments.FragmentTaskList;
import com.example.todoapp.util.ThemeManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ThemeManager.isDarkMode(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("ToDoApp");

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            int id = item.getItemId();

            if (id == R.id.nav_tasks) {
                selectedFragment = new FragmentTaskList();
            } else if (id == R.id.nav_calendar) {
                selectedFragment = new FragmentCalendar();
            } else {
                selectedFragment = new FragmentSettings();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });

        bottomNav.setSelectedItemId(R.id.nav_tasks);
    }
}
