package com.androidlab.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.adapter.TaskAdapter;
import com.androidlab.taskmaster.database.TaskDataBase;
import com.androidlab.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "title";
    public static final String TASK_BODY_TAG = "body";
    public static final String TASK_STATE_TAG = "state";
    public static final String DATABASE_NAME = "add_new_task";
    List<Task> tasks = new ArrayList<>();
    TaskAdapter adapter;
    TaskDataBase taskDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpListRecyclerView();
        Button addTaskButton = (Button) findViewById(R.id.addButton);
        Button allTasksButton = (Button) findViewById(R.id.allButton);
        Button settingsButton = (Button) findViewById(R.id.settingButton);
        taskDataBase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDataBase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        tasks = taskDataBase.taskDao().findAll();
        setUpListRecyclerView();
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


    }

    private void setUpListRecyclerView() {
        RecyclerView ListRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ListRecyclerView.setLayoutManager(layoutManager);
        adapter = new TaskAdapter(tasks, this);
        ListRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(SettingsActivity.USER_NAME, "NO USERNAME");
        ((TextView) findViewById(R.id.nametextView)).setText(getString(R.string.username_main, username));
        tasks = taskDataBase.taskDao().findAll();
        setUpListRecyclerView();
    }

}
