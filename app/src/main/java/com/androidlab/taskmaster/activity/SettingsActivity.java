package com.androidlab.taskmaster.activity;

import static com.androidlab.taskmaster.activity.TaskMasterAmplifyApplication.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.androidlab.taskmaster.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String USER_NAME = "username";
    public static final String TASK_TEAM_TAG = "team" ;
    Spinner teamSpinner = null;
    CompletableFuture<List<Team>> teamsFuture = new CompletableFuture<>();
    public String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button saveButton = findViewById(R.id.saveButton);
        setUpSpinner();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferneceEditor = sharedPreferences.edit();
                EditText username = (EditText) findViewById(R.id.task_title);
                String userNicknameString = username.getText().toString();
                String team = teamSpinner.getSelectedItem().toString();
                preferneceEditor.putString(USER_NAME, userNicknameString);
                preferneceEditor.putString(TASK_TEAM_TAG,team);
                preferneceEditor.apply();

                Snackbar.make(findViewById(R.id.settingsActivity), "Settings Saved", Snackbar.LENGTH_SHORT).show();
            }
        });
        Button backTaskButton = (Button) findViewById(R.id.backk_button2);
        backTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void setUpSpinner() {

        teamSpinner = (Spinner) findViewById(R.id.spinner2);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read Team Successfully");
                    ArrayList<String> teamName = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()) {
                        teams.add(team);
                        teamName.add(team.getTeamName());

                    }
                    teamsFuture.complete(teams);

                    runOnUiThread(() ->
                    {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamName));
                    });
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Did not read team successfully!");
                });
    }
    }