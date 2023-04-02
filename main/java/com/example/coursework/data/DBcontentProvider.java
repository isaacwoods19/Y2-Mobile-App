package com.example.coursework.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBcontentProvider extends ContentProvider {
    public static final int USERS = 100;
    public static final int TASKS = 101;
    public static final int PROFILE = 102;
    public static final int SELFASSESS = 103;
    public static final int COACH = 104;

    private static final UriMatcher myUriMatcher = buildUriMatcher();

    public static appDBHelper myDBHelper;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(DBcontract.CONTENT_AUTHORITY,DBcontract.PATH_USERS, USERS);

        matcher.addURI(DBcontract.CONTENT_AUTHORITY,DBcontract.PATH_TASKS, TASKS);

        matcher.addURI(DBcontract.CONTENT_AUTHORITY,DBcontract.PATH_PROFILE, PROFILE);

        matcher.addURI(DBcontract.CONTENT_AUTHORITY,DBcontract.PATH_SA, SELFASSESS);

        matcher.addURI(DBcontract.CONTENT_AUTHORITY,DBcontract.PATH_COACH, COACH);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        myDBHelper = new appDBHelper(getContext(), appDBHelper.DB_NAME, null, appDBHelper.DB_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match_code = myUriMatcher.match(uri);
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor myCursor;

        switch (match_code){
            case USERS:{
                myCursor = db.query(
                        DBcontract.UsersTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null,
                        null
                );
                break;
            }
            case TASKS:{
                myCursor = db.query(
                        DBcontract.TasksTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null,
                        null
                );
                break;
            }
            case PROFILE:{
                myCursor = db.query(
                        DBcontract.ProfileTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null,
                        null
                );
                break;
            }
            case SELFASSESS:{
                myCursor = db.query(
                        DBcontract.SATable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null,
                        null
                );
                break;
            }
            case COACH:{
                myCursor = db.query(
                        DBcontract.CoachTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null,
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        myCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return myCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //decided i didn't need to implement a type checker
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match_code = myUriMatcher.match(uri);
        Uri retUri = null;
        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        switch(match_code){
            case USERS:{
                long _id = db.insert(DBcontract.UsersTable.TABLE_NAME, null, values);
                if (_id > 0){
                    retUri = DBcontract.UsersTable.buildUriWithID(_id);
                }else{
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            case TASKS:{
                long _id = db.insert(DBcontract.TasksTable.TABLE_NAME, null, values);
                if (_id > 0){
                    retUri = DBcontract.TasksTable.buildUriWithID(_id);
                }else{
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            case PROFILE:{
                long _id = db.insert(DBcontract.ProfileTable.TABLE_NAME, null, values);
                if (_id > 0){
                    retUri = DBcontract.ProfileTable.buildUriWithID(_id);
                }else{
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            case SELFASSESS:{
                long _id = db.insert(DBcontract.SATable.TABLE_NAME, null, values);
                if (_id > 0){
                    retUri = DBcontract.SATable.buildUriWithID(_id);
                }else{
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            case COACH:{
                long _id = db.insert(DBcontract.CoachTable.TABLE_NAME, null, values);
                if (_id > 0){
                    retUri = DBcontract.CoachTable.buildUriWithID(_id);
                }else{
                    throw new SQLException("Failed to insert");
                }
                break;
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match_code = myUriMatcher.match(uri);
        int rowsDeleted = 0;

        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        switch (match_code){
            case USERS:{
                rowsDeleted = db.delete(DBcontract.UsersTable.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TASKS:{
                rowsDeleted = db.delete(DBcontract.TasksTable.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PROFILE:{
                rowsDeleted = db.delete(DBcontract.ProfileTable.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case SELFASSESS:{
                rowsDeleted = db.delete(DBcontract.SATable.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case COACH:{
                rowsDeleted = db.delete(DBcontract.CoachTable.TABLE_NAME, selection, selectionArgs);
                break;
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match_code = myUriMatcher.match(uri);
        int rowsUpdated = 0;

        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        switch (match_code){
            case USERS:{
                rowsUpdated = db.update(DBcontract.UsersTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case TASKS:{
                rowsUpdated = db.update(DBcontract.TasksTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case PROFILE:{
                rowsUpdated = db.update(DBcontract.ProfileTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case SELFASSESS:{
                rowsUpdated = db.update(DBcontract.SATable.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case COACH:{
                rowsUpdated = db.update(DBcontract.CoachTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
