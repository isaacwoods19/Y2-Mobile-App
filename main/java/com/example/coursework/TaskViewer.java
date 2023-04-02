package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.data.DBcontract;

public class TaskViewer extends AppCompatActivity {
    int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_viewer);
        setTitle("Badmin-Tracker");

        Intent task = getIntent();

        String areaText = task.getStringExtra("area");
        String descText = task.getStringExtra("description");
        taskID = task.getIntExtra("taskID", -1);

        TextView areaLabel = findViewById(R.id.taskArea);
        TextView descLabel = findViewById(R.id.taskDesc);

        areaLabel.setText(areaText);
        descLabel.setText(descText);
    }

    public void deleteTask(View view){
        //simply deletes the task from the database
        if (taskID != -1){
            getContentResolver().delete(DBcontract.TasksTable.CONTENT_URI, "taskID=?", new String[] {String.valueOf(taskID)});

            Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToCalendar(View view){
        //sends the user to the calendar app and automatically fills out an event with the task in it
        TextView areaLabel = findViewById(R.id.taskArea);
        TextView descLabel = findViewById(R.id.taskDesc);

        String title = "Badminton TODO - " + areaLabel.getText().toString();
        String description = descLabel.getText().toString();

        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title) // Simple title
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                .putExtra(CalendarContract.Events.DESCRIPTION, description) // Description
                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
        startActivity(insertCalendarIntent);
    }
}
