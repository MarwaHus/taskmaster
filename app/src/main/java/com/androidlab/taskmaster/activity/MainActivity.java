package com.androidlab.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.TaskEnum;
import com.androidlab.taskmaster.adapter.TaskAdapter;
import com.androidlab.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "title";
    public static final String TASK_BODY_TAG = "body";
    public static final String TASK_STATE_TAG = "state";
    List<Task> tasks = new ArrayList<>();
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpListRecyclerView();
        Button addTaskButton  =(Button)findViewById(R.id.addButton);
        Button allTasksButton =(Button)findViewById(R.id.allButton);
        Button settingsButton =(Button)findViewById(R.id.settingButton);
       // Button task1 =(Button)findViewById(R.id.task1);
        //Button task2 =(Button)findViewById(R.id.task2);
        //Button task3 =(Button)findViewById(R.id.task3);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllTasksActivity.class);
                startActivity(intent);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
       /* task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                intent.putExtra(TASK_TITLE_TAG, tasks.get(0).getTitle());
                intent.putExtra(TASK_BODY_TAG, tasks.get(0).getBody());
                intent.putExtra(TASK_STATE_TAG, tasks.get(0).getState().name());
                startActivity(intent);
            }
        });

        task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                intent.putExtra(TASK_TITLE_TAG, tasks.get(1).getTitle());
                intent.putExtra(TASK_BODY_TAG, tasks.get(1).getBody());
                intent.putExtra(TASK_STATE_TAG, tasks.get(1).getState().name());
                startActivity(intent);
            }
        });

        task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                intent.putExtra(TASK_TITLE_TAG, tasks.get(2).getTitle());
                intent.putExtra(TASK_BODY_TAG, tasks.get(2).getBody());
                intent.putExtra(TASK_STATE_TAG, tasks.get(2).getState().name());
                startActivity(intent);
            }
        });*/

    }

    private void setUpListRecyclerView() {
        RecyclerView ListRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ListRecyclerView.setLayoutManager(layoutManager);

        tasks.add(new Task("TASK 1", "STUDY", TaskEnum.complete));
        tasks.add(new Task("TASK 2", "WATCH MOVIE", TaskEnum.assigned));
        tasks.add(new Task("TASK 3", "DO MY ASSIGNMENT", TaskEnum.in_progress));
        tasks.add(new Task("TASK 4", "LEARN NEW THING", TaskEnum.New));
        tasks.add(new Task("TASK 5", "STUDY", TaskEnum.in_progress));
        tasks.add(new Task("TASK 6", "WATCH MOVIE", TaskEnum.complete));
        adapter = new TaskAdapter(tasks, this);
        ListRecyclerView.setAdapter(adapter);
    }



    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(SettingsActivity.USER_NAME, "NO USERNAME");
        ((TextView) findViewById(R.id.nametextView)).setText(getString(R.string.username_main, username));
    }

}
