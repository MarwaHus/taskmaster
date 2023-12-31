package com.androidlab.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidlab.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        String task = getIntent().getStringExtra(MainActivity.TASK_TITLE_TAG);
        Intent callingIntent = getIntent();
        String title = "";
        String body = "";
        String state = "";
        String team = "";
        if (callingIntent != null) {
            title = callingIntent.getStringExtra(MainActivity.TASK_TITLE_TAG);
            body = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            state = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);
            team = callingIntent.getStringExtra(MainActivity.TASK_TEAM_TAG);

        }

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView bodyTextView = findViewById(R.id.bodyTextView);
        TextView stateTextView = findViewById(R.id.stateTextView);
        TextView teamTextView = findViewById(R.id.teamTextView);

        titleTextView.setText(title);
        bodyTextView.setText(body);
        stateTextView.setText(state);
        teamTextView.setText(team);
        Button backTaskButton = findViewById(R.id.backk_button3);
        backTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}