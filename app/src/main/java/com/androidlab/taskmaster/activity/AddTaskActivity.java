package com.androidlab.taskmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.ProductCategoryEnum;
import com.amplifyframework.datastore.generated.model.Task;
import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.TaskEnum;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    private int totalTasks = 0;
    private TextView totalTasksTextView;
    public static final String TAG = "AddProductActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


       // totalTasks = taskDataBase.taskDao().findAll().size();
        totalTasksTextView = findViewById(R.id.textView5);
        totalTasksTextView.setText("Total Tasks: " + totalTasks);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
//            Task newTask = new Task(
//                    ((EditText) findViewById(R.id.task_title)).getText().toString(),
//                    ((EditText) findViewById(R.id.task_body)).getText().toString(),
//                    TaskEnum.fromString(((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString()),
//                    new Date());

          //  taskDataBase.taskDao().insertTask(newTask);

            String name = ((EditText)findViewById(R.id.task_title)).getText().toString();
            String description = ((EditText)findViewById(R.id.task_body)).getText().toString();
            Spinner taskSpinner = findViewById(R.id.spinner);
            taskSpinner.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    TaskEnum.values()));

            Task newProduct = Task.builder()
                    .name(name)
                    .description(description)
                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .productCategory((ProductCategoryEnum) taskSpinner.getSelectedItem()).build();

            Amplify.API.mutate(
                    ModelMutation.create(newProduct),
                    successResponse -> {
                        Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully");
                        totalTasks++;
                        runOnUiThread(() -> totalTasksTextView.setText("Total Tasks: " + totalTasks));
                        Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
                    },
                    failureResponse -> {
                        Log.e(TAG, "AddTaskActivity.onCreate(): failed with this response" + failureResponse);
                        Toast.makeText(this, "Failed to add task. Please try again.", Toast.LENGTH_SHORT).show();
                    }
            );
        });


        Button backButton = findViewById(R.id.backk_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}