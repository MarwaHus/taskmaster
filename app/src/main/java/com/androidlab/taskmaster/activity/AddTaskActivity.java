package com.androidlab.taskmaster.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidlab.taskmaster.R;

public class AddTaskActivity extends AppCompatActivity {
    private int totalTasks = 0;
    private TextView totalTasksTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        totalTasksTextView = findViewById(R.id.textView5);
        Button addTaskActivityBtn = (Button) findViewById(R.id.submit_button);
        addTaskActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(AddTaskActivity.this, "Task Added Successfully", Toast.LENGTH_SHORT);
                toast.show();
                totalTasks++;
                totalTasksTextView.setText("Total Tasks: " + totalTasks);
            }
        });

        Button backTaskButton = (Button) findViewById(R.id.backk_button);
        backTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}