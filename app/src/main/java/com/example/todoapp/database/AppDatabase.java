package com.example.todoapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.todoapp.model.Task;
import com.example.todoapp.dataaccess.TaskDataAccess;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract TaskDataAccess taskDataAccess();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "todo_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
