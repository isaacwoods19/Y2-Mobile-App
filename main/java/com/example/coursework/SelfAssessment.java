package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.coursework.data.DBcontract;

public class SelfAssessment extends AppCompatActivity {
    TextView seekLabel1;
    TextView seekLabel2;
    TextView seekLabel3;
    TextView seekLabel4;
    TextView seekLabel5;
    TextView seekLabel6;

    int progress1 = 5;
    int progress2 = 5;
    int progress3 = 5;
    int progress4 = 5;
    int progress5 = 5;
    int progress6 = 5;

    final int currentUserID = LogInActivity.currentUserID;
    Boolean SApresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_assessment);
        setTitle("Badmin-Tracker");
    }

    @Override
    protected void onResume() {
        super.onResume();

        //on start up, fetch the data from the DB
        String[] columns = {
                DBcontract.SATable.COLUMN_USERID,
                DBcontract.SATable.COLUMN_NET,
                DBcontract.SATable.COLUMN_DEFENSE,
                DBcontract.SATable.COLUMN_SERVE,
                DBcontract.SATable.COLUMN_REAR,
                DBcontract.SATable.COLUMN_MID,
                DBcontract.SATable.COLUMN_MOVE
        };
        // A cursor is your primary interface to the query results.
        Cursor cursor = getContentResolver().query(
                DBcontract.SATable.CONTENT_URI, // Table to Query
                columns,
                "userID=?", // Columns for the "where" clause
                new String[] {String.valueOf(currentUserID)}, // Values for the "where" clause
                null // sort Arg
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            //indicates that there is already a record in the DB
            SApresent = true;

            // Get the value in each column by finding the appropriate column index.
            int index = cursor.getColumnIndexOrThrow(DBcontract.SATable.COLUMN_NET);
            progress1 = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow(DBcontract.SATable.COLUMN_DEFENSE);
            progress2 = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow(DBcontract.SATable.COLUMN_REAR);
            progress3 = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow(DBcontract.SATable.COLUMN_MID);
            progress4 = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow(DBcontract.SATable.COLUMN_SERVE);
            progress5 = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow(DBcontract.SATable.COLUMN_MOVE);
            progress6 = cursor.getInt(index);
        }

        SeekBar seekBar1 = findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(seekBarChangeListener);
        seekLabel1 = findViewById(R.id.seekLabel1);
        seekLabel1.setText("Net play: " + progress1);
        seekBar1.setProgress(progress1);

        SeekBar seekBar2 = findViewById(R.id.seekBar2);
        seekBar2.setOnSeekBarChangeListener(seekBarChangeListener);
        seekLabel2 = findViewById(R.id.seekLabel2);
        seekLabel2.setText("Defense: " + progress2);
        seekBar2.setProgress(progress2);

        SeekBar seekBar3 = findViewById(R.id.seekBar3);
        seekBar3.setOnSeekBarChangeListener(seekBarChangeListener);
        seekLabel3 = findViewById(R.id.seekLabel3);
        seekLabel3.setText("Rear-court Attack: " + progress3);
        seekBar3.setProgress(progress3);

        SeekBar seekBar4 = findViewById(R.id.seekBar4);
        seekBar4.setOnSeekBarChangeListener(seekBarChangeListener);
        seekLabel4 = findViewById(R.id.seekLabel4);
        seekLabel4.setText("Mid-court Attack: " + progress4);
        seekBar4.setProgress(progress4);

        SeekBar seekBar5 = findViewById(R.id.seekBar5);
        seekBar5.setOnSeekBarChangeListener(seekBarChangeListener);
        seekLabel5 = findViewById(R.id.seekLabel5);
        seekLabel5.setText("Serving: " + progress5);
        seekBar5.setProgress(progress5);

        SeekBar seekBar6 = findViewById(R.id.seekBar6);
        seekBar6.setOnSeekBarChangeListener(seekBarChangeListener);
        seekLabel6 = findViewById(R.id.seekLabel6);
        seekLabel6.setText("Court Movement: " + progress6);
        seekBar6.setProgress(progress6);

    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (seekBar.getId()){
                case R.id.seekBar1:
                    seekLabel1.setText("Net play: " + progress);
                    break;

                case R.id.seekBar2:
                    seekLabel2.setText("Defense: " + progress);
                    break;

                case R.id.seekBar3:
                    seekLabel3.setText("Rear-court Attack: " + progress);
                    break;

                case R.id.seekBar4:
                    seekLabel4.setText("Mid-court Attack: " + progress);
                    break;

                case R.id.seekBar5:
                    seekLabel5.setText("Serving: " + progress);
                    break;

                case R.id.seekBar6:
                    seekLabel6.setText("Court Movement: " + progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //nothing
        }
    };

    public void onSubmit(View view){
        SeekBar seekBar1 = findViewById(R.id.seekBar1);
        SeekBar seekBar2 = findViewById(R.id.seekBar2);
        SeekBar seekBar3 = findViewById(R.id.seekBar3);
        SeekBar seekBar4 = findViewById(R.id.seekBar4);
        SeekBar seekBar5 = findViewById(R.id.seekBar5);
        SeekBar seekBar6 = findViewById(R.id.seekBar6);

        ContentValues values = new ContentValues();

        values.put("net", seekBar1.getProgress());
        values.put("defense", seekBar2.getProgress());
        values.put("rear", seekBar3.getProgress());
        values.put("mid", seekBar4.getProgress());
        values.put("serve", seekBar5.getProgress());
        values.put("move", seekBar6.getProgress());

        if (SApresent){
            //if theres already a record, then we need to update
            getContentResolver().update(DBcontract.SATable.CONTENT_URI, values, "userID=?", new String[] {String.valueOf(currentUserID)});

            Toast.makeText(this, "Self Assessment updated", Toast.LENGTH_SHORT).show();
        }else{
            //if not, then we need to insert
            values.put("userID", currentUserID);
            getContentResolver().insert(DBcontract.SATable.CONTENT_URI, values);

            Toast.makeText(this, "Self Assessment created", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
