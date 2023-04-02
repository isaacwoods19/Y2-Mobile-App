package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.example.coursework.data.DBcontract;

import java.util.ArrayList;
import java.util.Calendar;

public class LogInActivity extends AppCompatActivity {
    public static int currentUserID;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Badmin-Tracker");

        //schedules a notification to occur in one minute
        scheduleNotification(getNotification("Don't forget to add some tasks to do!")) ;
    }


    @Override
    protected void onResume() {
        super.onResume();

        //code for auto logging into the app if the preferences say to
        SharedPreferences mypref = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        String Username = mypref.getString("username", "user0");
        String Password = mypref.getString("password", "pass0");
        Boolean autoLogIn = mypref.getBoolean("autoLogIn", false);

        //if the saved username and password aren't default, and auto log in is true, then log in automatically
        if (!Username.equals("user0") && !Password.equals("pass0") && autoLogIn){
            EditText editText1 = findViewById(R.id.userText);
            EditText editText2 = findViewById(R.id.passwordText);

            editText1.setText(Username);
            editText2.setText(Password);

            RelativeLayout view = findViewById(R.id.loginView);

            logIn(view);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //code for storing the preferences for auto logging in
        SharedPreferences mypref = getSharedPreferences("com.example.coursework.LogInActivity", MODE_PRIVATE);

        //if auto log in is true, then save the username and password
        if (mypref.getBoolean("autoLogIn", false)){
            SharedPreferences.Editor editor = mypref.edit();
            editor.clear();

            EditText editText1 = findViewById(R.id.userText);
            EditText editText2 = findViewById(R.id.passwordText);

            editor.putString("username", editText1.getText().toString());
            editor.putString("password", editText2.getText().toString());
            editor.putBoolean("autoLogIn", true);

            editor.commit();
        }
    }

    public void logIn(View view){
        //activate auto log in for later now that they are logged in
        SharedPreferences mypref = getSharedPreferences("com.example.coursework.LogInActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = mypref.edit();
        editor.clear();
        editor.putBoolean("autoLogIn", true);
        editor.commit();

        //get the entered data
        EditText editText1 = findViewById(R.id.userText);
        EditText editText2 = findViewById(R.id.passwordText);
        String username = editText1.getText().toString();
        String password = editText2.getText().toString();

        //prepare database query
        String[] columns = {
                DBcontract.UsersTable.COLUMN_USERID,
                DBcontract.UsersTable.COLUMN_USERNAME,
                DBcontract.UsersTable.COLUMN_PASSWORD,
                DBcontract.UsersTable.COLUMN_COACH
        };
        //query the database to check if they are a user
        Cursor cursor = getContentResolver().query(
                DBcontract.UsersTable.CONTENT_URI, // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort Arg
        );

        ArrayList<String[]> usersDB = new ArrayList<>();

        // If possible, move to the first row of the query results, showing all users
        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.
            do{
                String[] user = new String[4];
                int index = cursor.getColumnIndexOrThrow(DBcontract.UsersTable.COLUMN_USERID);
                user[0] = String.valueOf(cursor.getInt(index));

                index = cursor.getColumnIndexOrThrow(DBcontract.UsersTable.COLUMN_USERNAME);
                user[1] = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(DBcontract.UsersTable.COLUMN_PASSWORD);
                user[2] = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(DBcontract.UsersTable.COLUMN_COACH);
                user[3] = cursor.getString(index);

                //adds all users to an array to search through
                usersDB.add(user);

            }while(cursor.moveToNext());

        }
        cursor.close();

        //simple boolean value for validation
        Boolean valid = false;
        //iterate through the array to check if the user is present
        for (int i=0;i<usersDB.size();i++){
            String[] user = usersDB.get(i);
            if (user[1].equals(username) && user[2].equals(password)){
                //they entered in the correct data
                valid = true;

                int isCoach;
                isCoach = Integer.parseInt(user[3]);

                currentUserID = Integer.parseInt(user[0]);

                //move the user to the main menu section
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("isCoach", isCoach);
                startActivity(intent);
            }
        }
        //simply shows a toast if the user doesnt enter the correct information
        if (!valid){
            String msg = "Username or password incorrect";
            Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    //method for signing up a new account
    public void signIn(View view){
        //checks if they want to be a coach
        RadioButton yesRad = findViewById(R.id.yes);
        //collect input data
        EditText editText1 = findViewById(R.id.userText);
        EditText editText2 = findViewById(R.id.passwordText);
        String username = editText1.getText().toString();
        String password = editText2.getText().toString();
        int isCoach;

        //prepare database query
        String[] columns = {
                DBcontract.UsersTable.COLUMN_USERID,
                DBcontract.UsersTable.COLUMN_USERNAME,
                DBcontract.UsersTable.COLUMN_PASSWORD,
                DBcontract.UsersTable.COLUMN_COACH
        };

        //checks if the username is already taken
        Cursor cursor = getContentResolver().query(
                DBcontract.UsersTable.CONTENT_URI, // Table to Query
                columns,
                "username=?", // Columns for the "where" clause
                new String[] {username}, // Values for the "where" clause
                null // sort Arg
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            //if it can, then the name is taken
            Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
            cursor.close();
        }else{
            cursor.close();
            //if it cant, then the name can be used
            if (yesRad.isChecked()){
                isCoach = 1;
            }else{
                isCoach = 0;
            }

            //preparing values for insertion
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            values.put("coach", isCoach);

            getContentResolver().insert(DBcontract.UsersTable.CONTENT_URI, values);


            if (isCoach == 1){
                //if they register as a coach, then also add them to the coach table
                //fetch their new userID
                Cursor cursor2 = getContentResolver().query(
                        DBcontract.UsersTable.CONTENT_URI, // Table to Query
                        columns,
                        "username=?", // Columns for the "where" clause
                        new String[] {username}, // Values for the "where" clause
                        null // sort Arg
                );

                int userID = 0;
                // If possible, move to the first row of the query results.
                if (cursor2.moveToFirst()) {
                    int index = cursor2.getColumnIndexOrThrow("userID");
                    userID = cursor2.getInt(index);
                }
                cursor2.close();

                //prepare values for insertion into coach table
                ContentValues values2 = new ContentValues();
                values2.put("coachID", userID);
                values2.put("coachName", username);

                getContentResolver().insert(DBcontract.CoachTable.CONTENT_URI, values2);
            }


            Toast toast = Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT);
            toast.show();
            //once done, it logs them in
            logIn(view);
        }
    }

    //Notification methods:
    //this schedules it to a certain time using AlarmManager
    private void scheduleNotification (Notification notification){
        Intent notificationIntent = new Intent( this, MyReceiver.class );
        notificationIntent.putExtra(MyReceiver.NOTIFICATION_ID , 1 );
        notificationIntent.putExtra(MyReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast ( this, 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE );

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 1);
        long afterOneMinute = c.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC , afterOneMinute , pendingIntent);
    }

    //this creates the notification itself
    private Notification getNotification (String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id);
        builder.setContentTitle("Badmin-Tracker");
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

    public void gotoUserGuide(View view){
        Intent intent = new Intent(LogInActivity.this, userGuide.class);
        startActivity(intent);
    }

}
