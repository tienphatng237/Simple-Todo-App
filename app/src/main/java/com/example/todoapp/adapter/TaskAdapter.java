package com.example.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.database.AppDatabase; // Ensure to import your AppDatabase
import com.example.todoapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks;

    public TaskAdapter(List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.title);
        holder.deadline.setText(task.deadline);
        holder.checkBox.setChecked(task.completed);

        // Delete button functionality
        holder.deleteButton.setOnClickListener(v -> {
            AppDatabase.getInstance(context).taskDataAccess().delete(task);
            tasks.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tasks.size());
        });

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.completed = isChecked;
            AppDatabase.getInstance(context).taskDataAccess().update(task);
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, deadline;
        CheckBox checkBox;
        ImageButton deleteButton;

        TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title);
            deadline = itemView.findViewById(R.id.task_deadline);
            checkBox = itemView.findViewById(R.id.task_checkbox);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}
