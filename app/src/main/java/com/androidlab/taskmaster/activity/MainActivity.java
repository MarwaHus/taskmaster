
package com.androidlab.taskmaster.activity;

import static com.androidlab.taskmaster.activity.AddTaskActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.androidlab.taskmaster.R;
import com.androidlab.taskmaster.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "title";
    public static final String TASK_BODY_TAG = "body";
    public static final String TASK_STATE_TAG = "state";
    public static final String TASK_TEAM_TAG = "team" ;
    public static final String TASK_ID_TAG = "TASK ID TAG" ;

    List<Task> tasks = new ArrayList<>();
    TaskAdapter adapter;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Amplify.Auth.signUp("marwa.hu18@gmail.com",
                "marwa@2023",
              AuthSignUpOptions.builder()
                      .userAttribute(AuthUserAttributeKey.email(),"marwa.hu18@gmail.com")
                      .userAttribute(AuthUserAttributeKey.nickname(),"M")
                      .build(),
                good ->
                {
                    Log.i(TAG, "Signup succeeded: "+ good.toString());
                },
                bad ->
                {
                    Log.i(TAG, "Signup failed with username: "+ "marwa.hu18@gmail.com"+ " with this message: "+ bad.toString());
                }
        );*/
        /*Amplify.Auth.confirmSignUp("marwa.hu18@gmail.com",
                "033206",
                success ->
                {
                    Log.i(TAG,"verification succeeded: "+ success.toString());

                },
                failure ->
                {
                    Log.i(TAG,"verification failed: "+ failure.toString());
                }
        );*/
        /*Amplify.Auth.signIn("marwa.hu18@gmail.com",
                "marwa@2023",
                success ->
                {
                    Log.i(TAG, "Login succeeded: "+success.toString());
                },
                failure ->
                {
                    Log.i(TAG, "Login failed: "+failure.toString());
                }
        );*/
       /* Amplify.Auth.signOut(
                () ->
                {
                    Log.i(TAG,"Logout succeeded");
                },
                failure ->
                {
                    Log.i(TAG, "Logout failed");
                }
        );*/
        init();
        setUpListRecyclerView();
        Button addTaskButton = (Button) findViewById(R.id.addButton);
        Button allTasksButton = (Button) findViewById(R.id.allButton);
        Button settingsButton = (Button) findViewById(R.id.settingButton);
        setUpListRecyclerView();
        setUpLoginAndLogoutButton();
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllTasksActivity.class);
                startActivity(intent);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setUpListRecyclerView() {
        RecyclerView ListRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ListRecyclerView.setLayoutManager(layoutManager);

//
//        Team team1 =
//                Team.builder()
//                    .teamName("Team1")
//                    .build();
//        Team team2 =
//                Team.builder()
//                        .teamName("Team2")
//                        .build();
//        Team team3 =
//                Team.builder()
//                        .teamName("Team3")
//                        .build();
//        Amplify.API.mutate(
//               ModelMutation.create(team1),
//               successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a team successfully"),
//               failureResponse -> Log.i(TAG, "MainActivity.onCreate(): team failed with this response: "+failureResponse)
//       );
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a team successfully"),
//                failureResponse -> Log.i(TAG, "MainActivity.onCreate(): team failed with this response: "+failureResponse)
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a team successfully"),
//                failureResponse -> Log.i(TAG, "MainActivity.onCreate(): team failed with this response: "+failureResponse)
//        );
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                success ->
                {
                    Log.i(TAG, "Read Task successfully");
                    tasks.clear();
                    for (com.amplifyframework.datastore.generated.model.Task databaseProduct : success.getData()){
                        tasks.add(databaseProduct);
                    }
                    runOnUiThread(() ->{
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read products successfully")
        );

        adapter = new TaskAdapter(tasks, this);
        ListRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
       // String username = preferences.getString(SettingsActivity.USER_NAME, "NO USERNAME");
        //((TextView) findViewById(R.id.nametextView)).setText(getString(R.string.username_main, username));
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String username="";
        if (authUser == null){
            Button loginButton = (Button) findViewById(R.id.loginTaskMasterButton);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton = (Button) findViewById(R.id.logoutTaskMasterButton);
            logoutButton.setVisibility(View.INVISIBLE);
        }else{
            username = authUser.getUsername();
            Log.i(TAG, "Username is: "+ username);
            Button loginButton = (Button) findViewById(R.id.loginTaskMasterButton);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton = (Button) findViewById(R.id.logoutTaskMasterButton);
            logoutButton.setVisibility(View.VISIBLE);

            String username2 = username; // ugly way for lambda hack
            Amplify.Auth.fetchUserAttributes(
                    success ->
                    {
                        Log.i(TAG, "Fetch user attributes succeeded for username: "+username2);
                        for (AuthUserAttribute userAttribute: success){
                            if(userAttribute.getKey().getKeyString().equals("email")){
                                String userEmail = userAttribute.getValue();
                                runOnUiThread(() ->
                                {
                                    ((TextView)findViewById(R.id.nametextView)).setText(userEmail);
                                });
                            }
                        }
                    },
                    failure ->
                    {
                        Log.i(TAG, "Fetch user attributes failed: "+failure.toString());
                    }
            );
        }
    setUpListRecyclerView();
    }
    private void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tasks = new ArrayList<>();
    }
    private void setUpLoginAndLogoutButton(){
        Button loginButton = (Button) findViewById(R.id.loginTaskMasterButton);
        loginButton.setOnClickListener(v ->
        {
            Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLogInIntent);
        });

        Button logoutButton = (Button) findViewById(R.id.logoutTaskMasterButton);
        logoutButton.setOnClickListener(v->
        {
            Amplify.Auth.signOut(
                    () ->
                    {
                        Log.i(TAG,"Logout succeeded");
                        runOnUiThread(() ->
                        {
                            ((TextView)findViewById(R.id.nametextView)).setText("");
                        });
                        Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(goToLogInIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Logout failed");
                        runOnUiThread(() ->
                        {
                            Toast.makeText(MainActivity.this, "Log out failed", Toast.LENGTH_LONG);
                        });
                    }
            );
        });
    }
}

