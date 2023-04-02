package com.example.coursework.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBcontract {

    public static final String CONTENT_AUTHORITY = "com.example.coursework";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_USERS = "users";
    public static final String PATH_TASKS = "tasks";
    public static final String PATH_PROFILE = "profiles";
    public static final String PATH_SA = "selfAssess";
    public static final String PATH_COACH = "coach";

    public static final class UsersTable implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        //Table Name
        public static final String TABLE_NAME = PATH_USERS;
        //Column Name
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_COACH = "coach";

        public static Uri buildUriWithID(long ID){
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }

    public static final class TasksTable implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        //Table Name
        public static final String TABLE_NAME = PATH_TASKS;
        //Column Name
        public static final String COLUMN_TASKID = "taskID";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_TAREA = "taskArea";
        public static final String COLUMN_TDESC = "taskDesc";
        public static final String COLUMN_COACH = "coachTask";

        public static Uri buildUriWithID(long ID){
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }

    public static final class ProfileTable implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build();

        //Table Name
        public static final String TABLE_NAME = PATH_PROFILE;
        //Column Name
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_YEARS = "years";
        public static final String COLUMN_ABOUT = "about";

        public static Uri buildUriWithID(long ID){
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }

    public static final class SATable implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SA).build();

        //Table Name
        public static final String TABLE_NAME = PATH_SA;
        //Column Name
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_NET = "net";
        public static final String COLUMN_DEFENSE = "defense";
        public static final String COLUMN_REAR = "rear";
        public static final String COLUMN_MID = "mid";
        public static final String COLUMN_SERVE = "serve";
        public static final String COLUMN_MOVE = "move";

        public static Uri buildUriWithID(long ID){
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }

    public static final class CoachTable implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COACH).build();

        //Table Name
        public static final String TABLE_NAME = PATH_COACH;
        //Column Name
        public static final String COLUMN_COACHID = "coachID";
        public static final String COLUMN_COACHNAME = "coachName";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_USERNAME = "userName";

        public static Uri buildUriWithID(long ID){
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }
}
