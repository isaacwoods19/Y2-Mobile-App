package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.data.DBcontract;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Badmin-Tracker");
    }

    @Override
    protected void onResume() {
        //everytime the activity is resumed, it refreshes the profile thats displayed to ensure its up to date
        super.onResume();

        TextView name = findViewById(R.id.forename);
        TextView surname = findViewById(R.id.surname);
        TextView height = findViewById(R.id.height);
        TextView weight = findViewById(R.id.weight);
        TextView years = findViewById(R.id.years);
        TextView about = findViewById(R.id.about);

        final int currentUserID = LogInActivity.currentUserID;

        Cursor cursor = checkForProfile(currentUserID);
        //if a profile exists, then display it and prepare to update it
        //if one doesn't, then let the user create one to be inserted

        if (cursor == null){
            //no profile in the database
            //so let user insert new one
            Button update = findViewById(R.id.button4);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertProfile(currentUserID);
                }
            });
        }else{
            //theres a profile in the database already
            //so populate the screen with this profile
            Button update = findViewById(R.id.button4);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateProfile(currentUserID);
                }
            });

            int index = cursor.getColumnIndexOrThrow("name");
            name.setText(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("surname");
            surname.setText(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("height");
            height.setText(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("weight");
            weight.setText(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("years");
            years.setText(cursor.getString(index));

            index = cursor.getColumnIndexOrThrow("about");
            about.setText(cursor.getString(index));
        }
    }

    public Cursor checkForProfile(int currentUserID) {

        //method for querying the database for if a profile is saved already
        String[] columns = {
                "userID",
                "name",
                "surname",
                "height",
                "weight",
                "years",
                "about"
        };
        // A cursor is your primary interface to the query results.
        Cursor cursor = getContentResolver().query(
                DBcontract.ProfileTable.CONTENT_URI, // Table to Query
                columns,
                "userID=?", // Columns for the "where" clause
                new String[] {String.valueOf(currentUserID)}, // Values for the "where" clause
                null // sort Arg
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            //return the cursor to be used
            return cursor;
        }else{
            return null;
        }
    }


    public void updateProfile(int currentUserID) {
        TextView name = findViewById(R.id.forename);
        TextView surname = findViewById(R.id.surname);
        TextView height = findViewById(R.id.height);
        TextView weight = findViewById(R.id.weight);
        TextView years = findViewById(R.id.years);
        TextView about = findViewById(R.id.about);

        ContentValues values = new ContentValues();

        values.put("name", name.getText().toString());
        values.put("surname", surname.getText().toString());
        values.put("height", height.getText().toString());
        values.put("weight", weight.getText().toString());
        values.put("years", years.getText().toString());
        values.put("about", about.getText().toString());

        getContentResolver().update(DBcontract.ProfileTable.CONTENT_URI, values, "userID=?", new String[] {String.valueOf(currentUserID)});

        Toast toast = Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT);
        toast.show();

        finish();
    }

    public void insertProfile(int currentUserID) {
        TextView name = findViewById(R.id.forename);
        TextView surname = findViewById(R.id.surname);
        TextView height = findViewById(R.id.height);
        TextView weight = findViewById(R.id.weight);
        TextView years = findViewById(R.id.years);
        TextView about = findViewById(R.id.about);

        ContentValues values = new ContentValues();

        values.put("userID", currentUserID);
        values.put("name", name.getText().toString());
        values.put("surname", surname.getText().toString());
        values.put("height", height.getText().toString());
        values.put("weight", weight.getText().toString());
        values.put("years", years.getText().toString());
        values.put("about", about.getText().toString());

        getContentResolver().insert(DBcontract.ProfileTable.CONTENT_URI, values);

        Toast toast = Toast.makeText(getApplicationContext(), "Profile created", Toast.LENGTH_SHORT);
        toast.show();

        finish();
    }
}
