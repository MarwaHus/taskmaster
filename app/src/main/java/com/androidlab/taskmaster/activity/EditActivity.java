package com.androidlab.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.ProductCategoryEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.adapter.TaskAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EditActivity extends AppCompatActivity {
    public static final String TAG = "editActivity";
    private CompletableFuture<com.amplifyframework.datastore.generated.model.Task> taskCompletableFuture = new CompletableFuture<>();
    private CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();
    private com.amplifyframework.datastore.generated.model.Task taskToEdit = null;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private Spinner taskCategorySpinner = null;
    private Spinner teamSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setUpEditableUIElement();
        setUpSaveButton();
        setUpDeleteButton();
    }

    private void setUpEditableUIElement() {
        Intent callingIntent = getIntent();
        String taskId = Objects.requireNonNull(getIntent().getStringExtra(MainActivity.TASK_ID_TAG));

        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                success -> {
                    Log.i(TAG, "Read tasks Successfully");
                    for (com.amplifyframework.datastore.generated.model.Task databaseTask : success.getData()) {
                        if (databaseTask.getId().equals(taskId)) {
                            taskCompletableFuture.complete(databaseTask);
                        }
                    }

                    runOnUiThread(() -> {
                    });
                },
                failure -> Log.i(TAG, "Did not read task successfully")
        );

        try {
            taskToEdit = taskCompletableFuture.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, "InterruptedException while getting task");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting task");
        }

        nameEditText = findViewById(R.id.editTask);
        nameEditText.setText(taskToEdit.getName());

        descriptionEditText = findViewById(R.id.editdesc);
        descriptionEditText.setText(taskToEdit.getDescription());

        setUpSpinners();
    }

    private void setUpSpinners() {
        teamSpinner = findViewById(R.id.spinner3);
        taskCategorySpinner = findViewById(R.id.spinner4);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read team successfully!");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()) {
                        teams.add(team);
                        teamNames.add(team.getTeamName());
                    }
                    teamFuture.complete(teams);

                    runOnUiThread(() -> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamNames));
                        teamSpinner.setSelection(getSpinnerIndex(teamSpinner, taskToEdit.getTeam().getTeamName()));
                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Did not read teams successfully!");
                }
        );

        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ProductCategoryEnum.values()));
        taskCategorySpinner.setSelection(getSpinnerIndex(taskCategorySpinner, taskToEdit.getProductCategory().toString()));
    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)) {
                return i;
            }
        }
        return 0;
    }

    private void setUpSaveButton() {
        Button saveButton = findViewById(R.id.updatebtn);
        saveButton.setOnClickListener(v -> {
            List<Team> teams = null;
            String teamToSaveString = teamSpinner.getSelectedItem().toString();
            try {
                teams = teamFuture.get();
            } catch (InterruptedException ie) {
                Log.e(TAG, "InterruptedException while getting teams");
                Thread.currentThread().interrupt();
            } catch (ExecutionException ee) {
                Log.e(TAG, "ExecutionException while getting teams");
            }
            Team teamToSave = Objects.requireNonNull(teams).stream()
                    .filter(c -> c.getTeamName().equals(teamToSaveString))
                    .findAny()
                    .orElseThrow(RuntimeException::new);

            com.amplifyframework.datastore.generated.model.Task taskToSave = com.amplifyframework.datastore.generated.model.Task.builder()
                    .name(nameEditText.getText().toString())
                    .id(taskToEdit.getId())
                    .description(descriptionEditText.getText().toString())
                    .team(teamToSave)
                    .productCategory(productCategoryFromString(taskCategorySpinner.getSelectedItem().toString()))
                    .build();

            Amplify.API.mutate(
                    ModelMutation.update(taskToSave),
                    successResponse -> {
                        Log.i(TAG, "EditActivity.onCreate(): edited a task successfully");
                        Snackbar.make(findViewById(R.id.editTask), "Task saved!", Snackbar.LENGTH_SHORT).show();
                    },
                    failureResponse -> Log.i(TAG, "EditActivity.onCreate(): failed with this response: " + failureResponse)
            );
        });
    }

    public static ProductCategoryEnum productCategoryFromString(String inputProductCategoryText) {
        for (ProductCategoryEnum productCategory : ProductCategoryEnum.values()) {
            if (productCategory.toString().equals(inputProductCategoryText)) {
                return productCategory;
            }
        }
        return null;
    }

    private void setUpDeleteButton() {
        Button deleteButton = findViewById(R.id.deletebtn);
        deleteButton.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        Amplify.API.mutate(
                ModelMutation.delete(taskToEdit),
                successResponse -> {
                    Log.i(TAG, "EditActivity.onCreate(): deleted a task successfully");
                    Intent goToTaskListActivity = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(goToTaskListActivity);
                },
                failureResponse -> Log.i(TAG, "EditActivity.onCreate(): failed with this response: " + failureResponse)
        );
    }

}

