package com.androidlab.taskmaster.activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.ProductCategoryEnum;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.androidlab.taskmaster.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    private int totalTasks = 0;
    private TextView totalTasksTextView;
    public static final String TAG = "AddProductActivity";
    Spinner teamSpinner = null;
    Spinner taskSpinner = null;

    CompletableFuture<List<Team>> teamsFuture = new CompletableFuture<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        totalTasksTextView = findViewById(R.id.textView5);
        totalTasksTextView.setText("Total Tasks: " + totalTasks);

        setUpSpinner();
        setUpSaveButton();

        Button backButton = findViewById(R.id.backk_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent callingIntent = getIntent();
        if (callingIntent != null && callingIntent.getType() != null && callingIntent.getType().equals("text/plain")) {
            String callingText = callingIntent.getStringExtra(Intent.EXTRA_TEXT);

            if (callingText != null) {
                String cleanedText = cleanText(callingText);
                ((EditText) findViewById(R.id.task_title)).setText(cleanedText);
            }
        }

        if(callingIntent != null && callingIntent.getType() != null && callingIntent.getType().startsWith("image") ){
            Uri incomingImageFileUri= callingIntent.getParcelableExtra(Intent.EXTRA_STREAM);

            if (incomingImageFileUri != null){
                InputStream incomingImageFileInputStream = null;

                try {
                    incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);

                    ImageView productImageView = findViewById(R.id.addImageView);

                    if (productImageView != null) {

                        productImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
                    }else {
                        Log.e(TAG, "ImageView is null for some reasons");
                    }
                }catch (FileNotFoundException fnfe){
                    Log.e(TAG," Could not get file stram from the URI "+fnfe.getMessage(),fnfe);
                }
            }
        }
    }
    public void setUpSpinner() {
        teamSpinner = findViewById(R.id.spinner5);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read Team Successfully");
                    ArrayList<String> teamNames = new ArrayList<>();
                    List<Team> teams = new ArrayList<>();

                    for (Team team : success.getData()) {
                        teams.add(team);
                        teamNames.add(team.getTeamName());
                    }

                    teamsFuture.complete(teams);

                    runOnUiThread(() -> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamNames));
                    });
                },
                failure -> {
                    teamsFuture.completeExceptionally(failure);
                    Log.e(TAG, "Did not read team successfully!", failure);
                }
        );

        taskSpinner = findViewById(R.id.spinner);
        taskSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ProductCategoryEnum.values()
        ));
    }

    private void setUpSaveButton() {
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.task_title)).getText().toString();
            String description = ((EditText) findViewById(R.id.task_body)).getText().toString();

            try {
                List<Team> teams = teamsFuture.get();
                String selectedTeamString = teamSpinner.getSelectedItem().toString();
                Team selectedTeam = teams.stream()
                        .filter(c -> c.getTeamName().equals(selectedTeamString))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Selected team not found"));

                Task newProduct = Task.builder()
                        .name(name)
                        .description(description)
                        .dateCreated(new Temporal.DateTime(new Date(), 0))
                        .productCategory((ProductCategoryEnum) taskSpinner.getSelectedItem())
                        .team(selectedTeam)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(newProduct),
                        successResponse -> {
                            Log.i(TAG, "Task added successfully");
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(MainActivity.TASK_TITLE_TAG, name);
                            resultIntent.putExtra(MainActivity.TASK_BODY_TAG, description);
                            setResult(MainActivity.RESULT_OK, resultIntent);
                            totalTasks++;
                            runOnUiThread(() -> totalTasksTextView.setText("Total Tasks: " + totalTasks));
                            Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        },
                        failureResponse -> {
                            Log.e(TAG, "Failed to add task", failureResponse);
                            Toast.makeText(this, "Failed to add task. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                );
                Snackbar.make(findViewById(R.id.addTaskActivity), "Task saved!", Snackbar.LENGTH_SHORT).show();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(TAG, "Error getting teams", e);
            }
        });
    }
    private String cleanText(String text) {
        text = text.replaceAll("\\b(?:https?|ftp):\\/\\/\\S+\\b", "");
        text = text.replaceAll("\"", "");
        return text;
    }
}