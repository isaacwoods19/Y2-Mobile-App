package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.example.coursework.data.DBcontract;

//activity for creating and displaying tasks
//this is done by having each task being a linear view within the table that is itself another linear view
//therefore allowing for a lot of flexibility
public class Tasks extends AppCompatActivity {
    final int currentUserID = LogInActivity.currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        setTitle("Badmin-Tracker");
    }

    @Override
    protected void onResume() {
        //every time the activity is resumed, the table is wiped and recreated
        //to have it contain all up to date tasks, even after creation and deletion
        super.onResume();

        LinearLayout layout = findViewById(R.id.layout);
        //clears the layout to be filled
        layout.removeAllViews();

        //this is the header layout, "Area, Description, Action"
        LinearLayout headerLayout = new LinearLayout(this);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerLayout.setLayoutParams(headerParams);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        //filling the header with the three headings
        //Area
        TextView areaHeading = new TextView(this);
        areaHeading.setText("Area");
        areaHeading.setTextSize(25);
        areaHeading.setTextColor(Color.parseColor("#000000"));
        areaHeading.setBackgroundColor(Color.parseColor("#b0b0b0"));
        areaHeading.setPadding(10,0,0,0);
        //Description
        TextView descHeading = new TextView(this);
        descHeading.setText("Description");
        descHeading.setTextSize(25);
        descHeading.setTextColor(Color.parseColor("#000000"));
        descHeading.setBackgroundColor(Color.parseColor("#b0b0b0"));
        descHeading.setPadding(10,0,0,0);
        //Action
        TextView actionHeading = new TextView(this);
        actionHeading.setText("Action");
        actionHeading.setTextSize(25);
        actionHeading.setTextColor(Color.parseColor("#000000"));
        actionHeading.setBackgroundColor(Color.parseColor("#b0b0b0"));
        actionHeading.setPadding(10,0,0,0);

        //Layout parameters for each of them
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

        //Now the actual tasks are added to the table

        //begin populating the tasks layout
        String[] columns = {
                DBcontract.TasksTable.COLUMN_TASKID,
                DBcontract.TasksTable.COLUMN_TAREA,
                DBcontract.TasksTable.COLUMN_TDESC
        };
        // A cursor is your primary interface to the query results.
        Cursor cursor = getContentResolver().query(
                DBcontract.TasksTable.CONTENT_URI, // Table to Query
                columns,
                "userID=? AND coachTask=?", // Columns for the "where" clause
                new String[] {String.valueOf(currentUserID), "0"}, // Values for the "where" clause
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

                //pass each task's data into this method to display it in the table
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //on button click, go to the activity that creates tasks
        Intent intent = new Intent(this, TaskCreater.class);
        intent.putExtra("currentUserID", currentUserID);
        intent.putExtra("coachTask", 0);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    public void onView(String areaText, String descText, int taskID){
        //takes task to activity where youy can view it, and have the option to delete
        //or add to calendar
        Intent intent = new Intent(Tasks.this, TaskViewer.class);
        intent.putExtra("area", areaText);
        intent.putExtra("description", descText);
        intent.putExtra("taskID", taskID);

        startActivity(intent);
    }
}
