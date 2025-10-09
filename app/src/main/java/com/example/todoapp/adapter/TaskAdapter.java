package com.example.todoapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase;
import com.example.todoapp.model.Task;
import com.example.todoapp.ui.EditTaskActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final AppDatabase db;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks, AppDatabase db) {
        this.context = context;
        this.db = db;
        this.tasks = (tasks != null) ? tasks : new ArrayList<>();
    }

    public void setTasks(List<Task> newTasks) {
        this.tasks = (newTasks != null) ? newTasks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.textTaskTitle.setText(task.getTitle());
        holder.textTaskDeadline.setText(task.getDeadline());

        if (task.getImagePath() != null && !task.getImagePath().isEmpty()) {
            File file = new File(task.getImagePath());
            if (file.exists())
                holder.imgTask.setImageURI(Uri.fromFile(file));
            else
                holder.imgTask.setImageResource(R.drawable.ic_image_placeholder);
        } else {
            holder.imgTask.setImageResource(R.drawable.ic_image_placeholder);
        }

        holder.checkBoxTask.setOnCheckedChangeListener(null);
        holder.checkBoxTask.setChecked(task.isCompleted());
        holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            Executors.newSingleThreadExecutor().execute(() -> db.taskDao().update(task));
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc muốn xoá công việc này?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.taskDao().delete(task);
                            tasks.remove(task);
                            ((android.app.Activity) context).runOnUiThread(() -> {
                                notifyDataSetChanged();
                                Toast.makeText(context, "Đã xoá công việc", Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("task_id", task.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (tasks != null) ? tasks.size() : 0;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxTask;
        TextView textTaskTitle, textTaskDeadline;
        ImageButton btnDelete;
        ImageView imgTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkboxTask);
            textTaskTitle = itemView.findViewById(R.id.textTaskTitle);
            textTaskDeadline = itemView.findViewById(R.id.textTaskDeadline);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgTask = itemView.findViewById(R.id.imgTask);
        }
    }
}
