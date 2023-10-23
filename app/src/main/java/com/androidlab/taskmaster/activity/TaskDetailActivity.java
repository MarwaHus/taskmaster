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
        TextView taskDetailsTextView = findViewById(R.id.taskDetails);
        String task = getIntent().getStringExtra("task");
        taskDetailsTextView.setText(task);



        Button backTaskButton = (Button) findViewById(R.id.backk_button3);
        backTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent callingIntent = getIntent();
        String title = null;
        String body = null;
        String state = null;


        if(callingIntent != null){
            title = callingIntent.getStringExtra(MainActivity.TASK_TITLE_TAG);
            body = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            state = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);

        }

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView bodyTextView = (TextView) findViewById(R.id.bodyTextView);
        TextView stateTextView = (TextView) findViewById(R.id.stateTextView);

}
    }
