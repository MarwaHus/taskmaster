package com.androidlab.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String USER_NAME="username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button saveButton= findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferneceEditor= sharedPreferences.edit();
                EditText username = (EditText) findViewById(R.id.task_title);
                String userNicknameString = username.getText().toString();
                preferneceEditor.putString(USER_NAME, userNicknameString);
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
}