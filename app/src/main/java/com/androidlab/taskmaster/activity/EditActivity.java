package com.androidlab.taskmaster.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.ProductCategoryEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.androidlab.taskmaster.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        activityResultLauncher = getImagePickingActivityResultLauncher();
        setUpEditableUIElement();
        setUpSaveButton();
        setUpDeleteButton();
        setUpAddImageButton();
        setUpDeleteImageButton();
        updateImageButtons();
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

        s3ImageKey = taskToEdit.getProductImageS3Key();
        if (s3ImageKey != null && !s3ImageKey.isEmpty())
        {
            Amplify.Storage.downloadFile(
                    s3ImageKey,
                    new File(getApplication().getFilesDir(), s3ImageKey),
                    success ->
                    {
                        ImageView taskImageView = findViewById(R.id.editImageView);
                        taskImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure ->
                    {
                        Log.e(TAG, "Unable to get image from S3 for the product for S3 key: " + s3ImageKey + " for reason: " + failure.getMessage());
                    }
            );
        }
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


    private void setUpSaveButton() {
        Button saveButton = (Button)findViewById(R.id.updatebtn);
        saveButton.setOnClickListener(v ->
        {
            saveTask(s3ImageKey);
        });
    }

    private void saveTask(String imageS3Key) {
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
                    .productImageS3Key(imageS3Key)
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

    private void setUpDeleteImageButton() {
        Button deleteImageButton = (Button)findViewById(R.id.deleteImageButton);
        String s3ImageKey = this.s3ImageKey;
        deleteImageButton.setOnClickListener(v ->
        {
            Amplify.Storage.remove(
                    s3ImageKey,
                    success ->
                    {
                        Log.i(TAG, "Succeeded in deleting file on S3! Key is: " + success.getKey());

                    },
                    failure ->
                    {
                        Log.e(TAG, "Failure in deleting file on S3 with key: " + s3ImageKey + " with error: " + failure.getMessage());
                    }
            );
            ImageView taskImageView = findViewById(R.id.editImageView);
            taskImageView.setImageResource(android.R.color.transparent);

            saveTask("");
            switchFromDeleteButtonToAddButton(deleteImageButton);
        });
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
    private void setUpDeleteButton() {
        Button deleteButton = findViewById(R.id.deletebtn);
        deleteButton.setOnClickListener(this::onClick);
    }
    private void setUpAddImageButton() {
        Button addImageButton = (Button) findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(b ->
        {
            launchImageSelectionIntent();
        });
    }
    private void launchImageSelectionIntent() {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imageFilePickingIntent);
    }
    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher() {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>()
                        {
                            @Override
                            public void onActivityResult(ActivityResult result)
                            {
                                Button addImageButton = findViewById(R.id.addImageButton);
                                if (result.getResultCode() == Activity.RESULT_OK)
                                {
                                    if (result.getData() != null)
                                    {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                            switchFromAddButtonToDeleteButton(addImageButton);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename,pickedImageFileUri);
                                        } catch (FileNotFoundException e)
                                        {
                                            Log.e(TAG, "Could not get file from file picker! " + e.getMessage(), e);
                                        }
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );

        return imagePickingActivityResultLauncher;
    }


    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename,Uri pickedImageFileUri)
    {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    saveTask(success.getKey());
                    updateImageButtons();
                   ImageView productImageView = findViewById(R.id.editImageView);
                    InputStream pickedImageInputStreamCopy = null;
                    try
                    {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    }
                    catch (FileNotFoundException e)
                    {
                        Log.e(TAG, "Could not get file stream from URI! " + e.getMessage(), e);
                    }
                    productImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));

                },
                failure ->
                {
                    Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage());
                }
        );
    }

    private void updateImageButtons() {
        Button addImageButton = findViewById(R.id.addImageButton);
        Button deleteImageButton = findViewById(R.id.deleteImageButton);
        runOnUiThread(() -> {
            if (s3ImageKey != null && !s3ImageKey.isEmpty()) {
                deleteImageButton.setVisibility(View.VISIBLE);
                addImageButton.setVisibility(View.INVISIBLE);
            } else {
                deleteImageButton.setVisibility(View.INVISIBLE);
                addImageButton.setVisibility(View.VISIBLE);
            }
        });
    }
    private void switchFromDeleteButtonToAddButton(Button deleteImageButton) {
        Button addImageButton = findViewById(R.id.addImageButton);
        deleteImageButton.setVisibility(View.INVISIBLE);
        addImageButton.setVisibility(View.VISIBLE);
    }

    private void switchFromAddButtonToDeleteButton(Button addImageButton) {
        Button deleteImageButton = findViewById(R.id.deleteImageButton);
        deleteImageButton.setVisibility(View.VISIBLE);
        addImageButton.setVisibility(View.INVISIBLE);
    }
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)) {
                return i;
            }
        }
        return 0;
    }
    public static ProductCategoryEnum productCategoryFromString(String inputProductCategoryText) {
        for (ProductCategoryEnum productCategory : ProductCategoryEnum.values()) {
            if (productCategory.toString().equals(inputProductCategoryText)) {
                return productCategory;
            }
        }
        return null;
    }
}

