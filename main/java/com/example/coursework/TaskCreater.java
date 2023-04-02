package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.example.coursework.data.DBcontract;

public class TaskCreater extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creater);
        setTitle("Badmin-Tracker");
    }

    public void addTask(View view){
        //simply gets the input data and writes it to the database
        EditText areaText = findViewById(R.id.areaText);
        EditText descText = findViewById(R.id.descriptionText);

        String areaString = areaText.getText().toString();
        String descString = descText.getText().toString();

        Intent intent = getIntent();
        int userID = intent.getIntExtra("currentUserID", 0);
        int coachTask = intent.getIntExtra("coachTask", 0);

        if (userID != 0){
            ContentValues values = new ContentValues();
            values.put(DBcontract.TasksTable.COLUMN_USERID, userID);
            values.put(DBcontract.TasksTable.COLUMN_TAREA, areaString);
            values.put(DBcontract.TasksTable.COLUMN_TDESC, descString);
            values.put(DBcontract.TasksTable.COLUMN_COACH, coachTask);

            getContentResolver().insert(DBcontract.TasksTable.CONTENT_URI, values);
        }

        finish();
    }
}
