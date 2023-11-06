package com.androidlab.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.activity.MainActivity;
import com.androidlab.taskmaster.activity.TaskDetailActivity;
import com.androidlab.taskmaster.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskListViewHolder> {
    private List<Task> taskList;
    private Context context;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);
        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        TextView taskTextViewFragment = holder.itemView.findViewById(R.id.textViewTaskFragment);
        String title = taskList.get(position).getTitle();
        String body = taskList.get(position).getBody();
        String state = taskList.get(position).getState().name();
        String taskInfo = "Title: " + title;
        taskTextViewFragment.setText(taskInfo);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, TaskDetailActivity.class);
            intent.putExtra(MainActivity.TASK_TITLE_TAG, title);
            intent.putExtra(MainActivity.TASK_BODY_TAG, body);
            intent.putExtra(MainActivity.TASK_STATE_TAG, state.toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}