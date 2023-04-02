package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.data.DBcontract;

public class Coach extends AppCompatActivity {
    final int currentUserID = LogInActivity.currentUserID;
    int isCoach;

    int coachID;
    String coachName;
    int userID = -1;
    String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);
        setTitle("Badmin-Tracker");

        Intent intent = getIntent();
        isCoach = intent.getIntExtra("isCoach", 0);

        TextView coachLabel = findViewById(R.id.coachLabel2);
        TextView tableLabel = findViewById(R.id.textView3);
        Button button = findViewById(R.id.applyButton);

        if (isCoach == 1){
            //if the user is a coach

            Cursor cursor = getContentResolver().query(
                    DBcontract.CoachTable.CONTENT_URI,
                    new String[] {"coachID", "coachName", "userID", "userName"},
                    "coachID=?",
                    new String[] {String.valueOf(currentUserID)},
                    null
            );

            if (cursor.moveToFirst()){
                int index = cursor.getColumnIndexOrThrow("coachID");
                coachID = cursor.getInt(index);

                index = cursor.getColumnIndexOrThrow("coachName");
                coachName = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow("userID");
                userID = cursor.getInt(index);

                index = cursor.getColumnIndexOrThrow("userName");
                userName = cursor.getString(index);
            }

            if (userName == null || userID == -1){
                coachLabel.setText("You currently aren't assigned to anyone");
                button.setVisibility(View.GONE);
            }else{
                coachLabel.setText("Assign ticket to " + userName);
                tableLabel.setText(userName + "'s tasks:");
                button.setText("ASSIGN");
                final int finalUserID = userID;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Coach.this, TaskCreater.class);
                        intent.putExtra("currentUserID", finalUserID);
                        intent.putExtra("coachTask", 1);
                        startActivity(intent);
                    }
                });
            }

        }else if (isCoach == 0){
            //if the user isn't a coach

            //check if they have a coach assigned
            Cursor cursor = getContentResolver().query(
                    DBcontract.CoachTable.CONTENT_URI,
                    new String[] {"coachID", "coachName", "userID", "userName"},
                    "userID=?",
                    new String[] {String.valueOf(currentUserID)},
                    null
            );

            if (cursor.moveToFirst()) {
                //then they have a coach assigned
                int index = cursor.getColumnIndexOrThrow("coachID");
                coachID = cursor.getInt(index);

                index = cursor.getColumnIndexOrThrow("coachName");
                coachName = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow("userID");
                userID = cursor.getInt(index);

                index = cursor.getColumnIndexOrThrow("userName");
                userName = cursor.getString(index);

                coachLabel.setText("Your coach is " + coachName);
                button.setVisibility(View.GONE);

            }else{
                //then they don't have a coach assigned
                //therefore nothing changes from the default page
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayout layout = findViewById(R.id.layout);
        //clears the layout to be filled
        layout.removeAllViews();

        //this is the header layout
        LinearLayout headerLayout = new LinearLayout(this);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerLayout.setLayoutParams(headerParams);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        //filling the header with the three headings
        TextView areaHeading = new TextView(this);
        areaHeading.setText("Area");
        areaHeading.setTextSize(25);
        areaHeading.setTextColor(Color.parseColor("#000000"));
        areaHeading.setBackgroundColor(Color.parseColor("#b0b0b0"));
        areaHeading.setPadding(10,0,0,0);
        TextView descHeading = new TextView(this);
        descHeading.setText("Description");
        descHeading.setTextSize(25);
        descHeading.setTextColor(Color.parseColor("#000000"));
        descHeading.setBackgroundColor(Color.parseColor("#b0b0b0"));
        descHeading.setPadding(10,0,0,0);
        TextView actionHeading = new TextView(this);
        actionHeading.setText("Action");
        actionHeading.setTextSize(25);
        actionHeading.setTextColor(Color.parseColor("#000000"));
        actionHeading.setBackgroundColor(Color.parseColor("#b0b0b0"));
        actionHeading.setPadding(10,0,0,0);

        LinearLayout.LayoutParams headingsParams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
        headingsParams1.setMargins(1, 0, 1, 0);
        areaHeading.setLayoutParams(headingsParams1);
        actionHeading.setLayoutParams(headingsParams1);

        LinearLayout.LayoutParams headingsParams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
        headingsParams2.setMargins(1, 0, 1, 0);
        descHeading.setLayoutParams(headingsParams2);

        headerLayout.addView(areaHeading);
        headerLayout.addView(descHeading);
        headerLayout.addView(actionHeading);
        //header is added
        layout.addView(headerLayout);

        //begin populating the tasks layout
        String[] columns = {
                DBcontract.TasksTable.COLUMN_TASKID,
                DBcontract.TasksTable.COLUMN_TAREA,
                DBcontract.TasksTable.COLUMN_TDESC
        };

        //this is to display the tasks the coach has set to the coach
        int ID = 0;
        if (isCoach == 1){
            ID = userID;

        }else if (isCoach == 0){
            ID = currentUserID;
        }

        // A cursor is your primary interface to the query results.
        Cursor cursor = getContentResolver().query(
                DBcontract.TasksTable.CONTENT_URI, // Table to Query
                columns,
                "userID=? AND coachTask=?", // Columns for the "where" clause
                new String[] {String.valueOf(ID), "1"}, // Values for the "where" clause
                null // sort Arg
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.
            do{
                int index = cursor.getColumnIndexOrThrow(DBcontract.TasksTable.COLUMN_TASKID);
                int taskID = cursor.getInt(index);

                index = cursor.getColumnIndexOrThrow(DBcontract.TasksTable.COLUMN_TAREA);
                String tArea = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(DBcontract.TasksTable.COLUMN_TDESC);
                String tDesc = cursor.getString(index);

                populateTasks(tArea, tDesc, taskID);

            }while(cursor.moveToNext());
        }else{
            Toast.makeText(this, "You currently have no tasks", Toast.LENGTH_LONG).show();
        }

    }

    public void populateTasks(final String areaText, final String descText, final int taskID){
        //populate the tasks table with tasks from the database
        LinearLayout layout = findViewById(R.id.layout);

        LinearLayout.LayoutParams areaParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
        areaParams.setMargins(2,2,2,2);

        TextView area1 = new TextView(this);
        area1.setText(areaText);
        area1.setBackgroundColor(Color.parseColor("#ffffff"));
        area1.setLayoutParams(areaParams);
        area1.setPadding(10,10,10,10);


        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
        descParams.setMargins(2,0,2,50);

        TextView description1 = new TextView(this);
        description1.setText(descText);
        description1.setBackgroundColor(Color.parseColor("#ffffff"));
        description1.setLayoutParams(descParams);
        description1.setPadding(5,5,5,5);


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);
        buttonParams.setMargins(2,2,2,2);

        Button view1 = new Button(this);
        view1.setText("View");
        view1.setLayoutParams(buttonParams);
        view1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                onView(areaText, descText, taskID);
            }
        });

        LinearLayout newRow = new LinearLayout(this);
        LinearLayout.LayoutParams rowLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newRow.setLayoutParams(rowLayout);
        newRow.setOrientation(LinearLayout.HORIZONTAL);
        newRow.addView(area1);
        newRow.addView(description1);
        newRow.addView(view1);

        layout.addView(newRow);
    }

    public void onApply(View view){
        Toast toast = Toast.makeText(this, "Your application has been noted, an admin will assign you a coach soon", Toast.LENGTH_LONG);
        toast.show();
    }

    public void onView(String areaText, String descText, int taskID){
        Intent intent = new Intent(Coach.this, TaskViewer.class);
        intent.putExtra("area", areaText);
        intent.putExtra("description", descText);
        intent.putExtra("taskID", taskID);

        startActivity(intent);
    }
}
