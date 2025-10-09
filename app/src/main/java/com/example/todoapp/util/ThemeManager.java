package com.example.todoapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeManager {

    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    public static boolean isDarkMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkMode(Context context, boolean enabled) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }
}
