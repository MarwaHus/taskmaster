package com.androidlab.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.GraphQLOperation;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.ProductCategoryEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.model.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EditActivity extends AppCompatActivity {
    public static final String TAG= "editActivity";
    private CompletableFuture<Task> taskCompletableFuture=null;
    private CompletableFuture<List<Team>> contactFuture = null;
    private Task taskToEdit= null;
    private EditText nameEditText;
    private EditText descriptionEditText;

    private Spinner taskCategorySpinner = null;

    private Spinner teamSpinner = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }
    private void setUpEditableUIElement() {
        Intent callingIntent = getIntent();
        String taskId = null;

        if(callingIntent != null){
            taskId = callingIntent.getStringExtra(MainActivity.TASK_TITLE_TAG);
        }

        String taskId2 = taskId;

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success ->
                {
                    Log.i(TAG, "Read tasks Successfully");

                    for (Task databasetask : success.getData()) {
                        if (databasetask.getId().equals(taskId2)) {
                            taskCompletableFuture.complete(databasetask);
                        }
                    }

                    runOnUiThread(() ->
                    {
                    });
                },
                failure -> Log.i(TAG, "Did not read task successfully")
        );

        try {
            taskToEdit = taskCompletableFuture.get();
        }catch (InterruptedException ie){
            Log.e(TAG, "InterruptedException while getting task");
            Thread.currentThread().interrupt();
        }catch (ExecutionException ee){
            Log.e(TAG, "ExecutionException while getting task");
        }

        nameEditText = ((EditText) findViewById(R.id.editTask));
        nameEditText.setText(taskToEdit.getTitle());
        descriptionEditText = ((EditText) findViewById(R.id.editdesc));
        descriptionEditText.setText(taskToEdit.getBody());
        setUpSpinners();
    }

    private void setUpSpinners()
    {
        teamSpinner = (Spinner) findViewById(R.id.spinner3);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read contacts successfully!");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Team> contacts = new ArrayList<>();
                    for (Team team : success.getData())
                    {
                        contacts.add(team);
                        contactNames.add(team.getTeamName());
                    }
                    contactFuture.complete(contacts);

                    runOnUiThread(() ->
                    {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactNames));
                        teamSpinner.setSelection(getSpinnerIndex(teamSpinner, taskToEdit.getBody()));
                    });
                },
                failure -> {
                    contactFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully!");
                }
        );

        taskCategorySpinner = (Spinner) findViewById(R.id.spinner3);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ProductCategoryEnum.values()));
       teamSpinner.setSelection(getSpinnerIndex(taskCategorySpinner, taskToEdit.getState().toString()));
    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck){
        for (int i = 0;i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)){
                return i;
            }
        }

        return 0;
    }

    private void setUpSaveButton()
    {
        Button saveButton = (Button)findViewById(R.id.updatebtn);
        saveButton.setOnClickListener(v ->
        {
            List<Team> contacts = null;
            String contactToSaveString = teamSpinner.getSelectedItem().toString();
            try
            {
                contacts = contactFuture.get();
            }
            catch (InterruptedException ie)
            {
                Log.e(TAG, "InterruptedException while getting product");
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException ee)
            {
                Log.e(TAG, "ExecutionException while getting product");
            }
            Team contactToSave = contacts.stream().filter(c -> c.getTeamName().equals(contactToSaveString)).findAny().orElseThrow(RuntimeException::new);
            com.amplifyframework.datastore.generated.model.Task productToSave = Task.builder()
                    .name(nameEditText.getText().toString())
                    .id(taskToEdit.getId())
                    .dateCreated(taskToEdit.getDateCreated())
                    .description(descriptionEditText.getText().toString())
                    .team(contactToSave)
                    .productCategory(productCategoryFromString(taskCategorySpinner.getSelectedItem().toString()))
                    .build();

            Amplify.API.mutate(
                    ModelMutation.update(productToSave),
                    successResponse ->
                    {
                        Log.i(TAG, "EditMainActivity.onCreate(): edited a task successfully");
                        Snackbar.make(findViewById(R.id.editTask), "Product saved!", Snackbar.LENGTH_SHORT).show();
                    },
                    failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)  // failure callback
            );
        });
    }

    public static ProductCategoryEnum productCategoryFromString(String inputProductCategoryText){
        for (ProductCategoryEnum productCategory : ProductCategoryEnum.values()){
            if(productCategory.toString().equals(inputProductCategoryText)){
                return productCategory;
            }
        }
        return null;
    }

    private void setUpDeleteButton(){
        Button deleteButton = (Button) findViewById(R.id.deletebtn);
        deleteButton.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        Amplify.API.mutate(
                ModelMutation.delete(taskToEdit),
                successResponse ->
                {
                    Log.i(TAG, "EditMainActivity.onCreate(): deleted a task successfully");
                    Intent goToProductListActivity = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(goToProductListActivity);
                },
                failureResponse -> Log.i(TAG, "EditMainActivity.onCreate(): failed with this response: " + failureResponse)
        );
    }
}
