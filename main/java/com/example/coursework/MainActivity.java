package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//main menu of the app
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Badmin-Tracker");
        TextView textView = (TextView) findViewById(R.id.welcomeText);

        //fetch user info from log in
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int isCoach = intent.getIntExtra("isCoach", 0);

        String welcomeText;
        if(isCoach == 1){
            welcomeText = "Welcome, Coach " + username;
        }else{
            welcomeText = "Welcome, " + username;
        }
        textView.setText(welcomeText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creates a button to log out in the action bar
        getMenuInflater().inflate(R.menu.log_out_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //when log out button clicked
        Toast toast = Toast.makeText(getApplicationContext(), "You have logged out", Toast.LENGTH_SHORT);
        toast.show();

        //turns off auto log in as the user is now logged out
        SharedPreferences mypref = getSharedPreferences("com.example.coursework.LogInActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = mypref.edit();
        editor.clear();

        editor.putBoolean("autoLogIn", false);

        editor.commit();

        LogInActivity.currentUserID = -1;

        //closes main menu and returns to log in section
        this.finish();

        return super.onOptionsItemSelected(item);
    }

    public void gotoProfile(View view){
        //on profile button click, goes to profile activity
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }

    public void gotoTasks(View view){
        //on tasks button click, goes to tasks activity
        Intent intent = new Intent(MainActivity.this, Tasks.class);
        startActivity(intent);
    }

    public void gotoSelfAssess(View view){
        //on self assess button click, goes to self assess activity
        Intent intent = new Intent(MainActivity.this, SelfAssessment.class);
        startActivity(intent);
    }

    public void gotoCoach(View view){
        //on coach button click, goes to button activity
        //which changes depending on if theyre a coach or not
        Intent previousintent = getIntent();
        int isCoach = previousintent.getIntExtra("isCoach", 0);

        Intent intent = new Intent(MainActivity.this, Coach.class);
        intent.putExtra("isCoach", isCoach);
        startActivity(intent);
    }
}
