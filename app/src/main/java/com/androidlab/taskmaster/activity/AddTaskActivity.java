package com.androidlab.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.TaskEnum;
import com.androidlab.taskmaster.database.TaskDataBase;
import com.androidlab.taskmaster.model.Task;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    private int totalTasks = 0;
    private TextView totalTasksTextView;
    TaskDataBase taskDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskDataBase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDataBase.class,
                        "add_new_task")
                .allowMainThreadQueries()
                .build();

        totalTasks = taskDataBase.taskDao().findAll().size();
        totalTasksTextView = findViewById(R.id.textView5);
        totalTasksTextView.setText("Total Tasks: " + totalTasks);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            Task newTask = new Task(
                    ((EditText) findViewById(R.id.task_title)).getText().toString(),
                    ((EditText) findViewById(R.id.task_body)).getText().toString(),
                    TaskEnum.fromString(((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString()),
                    new Date());
            taskDataBase.taskDao().insertTask(newTask);
            totalTasks++;
            totalTasksTextView.setText("Total Tasks: " + totalTasks);
            Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
        });
        Spinner taskSpinner = findViewById(R.id.spinner);
        taskSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskEnum.values()));

        Button backButton = findViewById(R.id.backk_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}