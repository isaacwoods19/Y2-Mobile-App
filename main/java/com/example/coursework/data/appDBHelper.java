package com.example.coursework.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class appDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "users.db";

    public SQLiteDatabase myDB;

    public appDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        myDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        myDB = db;

        String usersTable = "CREATE TABLE IF NOT EXISTS " + DBcontract.UsersTable.TABLE_NAME + " (" + DBcontract.UsersTable.COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBcontract.UsersTable.COLUMN_USERNAME + " TEXT NOT NULL, " + DBcontract.UsersTable.COLUMN_PASSWORD + " TEXT NOT NULL, " + DBcontract.UsersTable.COLUMN_COACH + " BOOLEAN NOT NULL);";
        String tasksTable = "CREATE TABLE IF NOT EXISTS " + DBcontract.TasksTable.TABLE_NAME + " (" + DBcontract.TasksTable.COLUMN_TASKID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBcontract.TasksTable.COLUMN_USERID + " INTEGER NOT NULL, " + DBcontract.TasksTable.COLUMN_TAREA + " TEXT NOT NULL, " + DBcontract.TasksTable.COLUMN_TDESC + " TEXT NOT NULL, " + DBcontract.TasksTable.COLUMN_COACH + " BOOLEAN NOT NULL);";
        String profileTable = "CREATE TABLE IF NOT EXISTS " + DBcontract.ProfileTable.TABLE_NAME + " (" + DBcontract.ProfileTable.COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBcontract.ProfileTable.COLUMN_NAME + " TEXT NOT NULL, " + DBcontract.ProfileTable.COLUMN_SURNAME + " TEXT NOT NULL, " + DBcontract.ProfileTable.COLUMN_HEIGHT + " FLOAT NOT NULL, " + DBcontract.ProfileTable.COLUMN_WEIGHT + " FLOAT NOT NULL, " + DBcontract.ProfileTable.COLUMN_YEARS + " INTEGER NOT NULL, " + DBcontract.ProfileTable.COLUMN_ABOUT + " TEXT NOT NULL);";
        String selfAssessTable = "CREATE TABLE IF NOT EXISTS " + DBcontract.SATable.TABLE_NAME + " (" + DBcontract.SATable.COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBcontract.SATable.COLUMN_NET + " INTEGER NOT NULL, " + DBcontract.SATable.COLUMN_DEFENSE + " INTEGER NOT NULL, " + DBcontract.SATable.COLUMN_REAR + " INTEGER NOT NULL, " + DBcontract.SATable.COLUMN_MID + " INTEGER NOT NULL, " + DBcontract.SATable.COLUMN_SERVE + " INTEGER NOT NULL, " + DBcontract.SATable.COLUMN_MOVE + " INTEGER NOT NULL);";
        String coachTable = "CREATE TABLE IF NOT EXISTS " + DBcontract.CoachTable.TABLE_NAME + " (" + DBcontract.CoachTable.COLUMN_COACHID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBcontract.CoachTable.COLUMN_COACHNAME + " TEXT NOT NULL, " + DBcontract.CoachTable.COLUMN_USERID + " INTEGER, " + DBcontract.CoachTable.COLUMN_USERNAME + " TEXT);";

        db.execSQL(usersTable);
        db.execSQL(tasksTable);
        db.execSQL(profileTable);
        db.execSQL(selfAssessTable);
        db.execSQL(coachTable);

        //inserting default data ready to be assessed
        ContentValues val = new ContentValues();
        val.put("userID", 1);
        val.put("username", "eee");
        val.put("password", "eee");
        val.put("coach", 1);

        db.insert(DBcontract.UsersTable.TABLE_NAME, null, val);
        val.clear();

        //not a coach
        val.put("userID", 2);
        val.put("username", "testUser");
        val.put("password", "eee");
        val.put("coach", 0);

        db.insert(DBcontract.UsersTable.TABLE_NAME, null, val);
        val.clear();

        //adding their relation to the coach table
        val.put("coachID", 1);
        val.put("username", "eee");
        val.put("userID", 2);
        val.put("userName", "testUser");

        db.insert(DBcontract.CoachTable.TABLE_NAME, null, val);
        val.clear();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DBcontract.UsersTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBcontract.TasksTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBcontract.ProfileTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBcontract.SATable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBcontract.CoachTable.TABLE_NAME);

        onCreate(db);
    }
}
