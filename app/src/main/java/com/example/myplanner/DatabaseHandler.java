package com.example.myplanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/*************************************************************************************************************************
 The purpose of this handler class is to help in handling the Database file. It extends the "SQLiteOpenHelper" class
 because the latter is the main class used for creating and managing our SQLite database.
 - There are 3 tables stored in the database file:
    - Table 1: Stores all the current/active tasks
    - Table 2: Stores all the expired tasks
    - Table 3: Stores all the tasks that have recently expired AND need to be reviewed
 ************************************************************************************************************************/

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myPlanner.db";

    // This 1st table is used for storing the current/active tasks being performed.
    public static final String TABLE1 = "Current_Tasks_Table";
    public static final String TABLE1_COL1 = "Tasks";
    public static final String TABLE1_COL2 = "Descriptions";
    public static final String TABLE1_COL3 = "Request_Codes";
    public static final String TABLE1_COL4 = "Time";

    // This 2nd table is used for storing the expired tasks.
    public static final String TABLE2 = "Expired_Tasks_Table";
    public static final String TABLE2_COL1 = "Tasks";
    public static final String TABLE2_COL2 = "Descriptions";
    public static final String TABLE2_COL3 = "Request_Codes";
    public static final String TABLE2_COL4 = "Time";
    public static final String TABLE2_COL5 = "Review";
    public static final String TABLE2_COL6 = "Grade";

    // This 3rd table is used for storing tasks that have recently expired AND need to be reviewed.
    public static final String TABLE3 = "ReviewDialog_Table";
    public static final String TABLE3_COL1 = "Tasks";
    public static final String TABLE3_COL2 = "Descriptions";
    public static final String TABLE3_COL3 = "Request_Codes";
    public static final String TABLE3_COL4 = "Time";


    // The database for this app gets created when this constructor gets called.
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 6); // This line creates the database.
    }

    // When the "onCreate" method gets called, the tables for the database gets created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE1 + " (Tasks TEXT, Descriptions TEXT, Request_Codes INTEGER, Time TEXT)");
        db.execSQL("create table " + TABLE2 + " (Tasks TEXT, Descriptions TEXT, Request_Codes INTEGER, Time TEXT, Review TEXT, Grade TEXT)");
        db.execSQL("create table " + TABLE3 + " (Tasks TEXT, Descriptions TEXT, Request_Codes INTEGER, Time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE1);
        db.execSQL("drop table if exists " + TABLE2);
        db.execSQL("drop table if exists " + TABLE3);
        onCreate(db);
    }


    // This method is used to insert a new task into table 1. The method returns a boolean in order for us to check if
    // the task was successfully inserted into the table.
    public boolean insertIntoTable1(String task, String description, int requestCode, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues myContentValues =  new ContentValues();

        myContentValues.put(TABLE1_COL1, task);
        myContentValues.put(TABLE1_COL2, description);
        myContentValues.put(TABLE1_COL3, requestCode);
        myContentValues.put(TABLE1_COL4, time);

        // If the data passed in wasn't inserted in the db, then the insert method (below) returns "-1".
        // Otherwise, it returns the ID of the newly inserted row.
        long rowID = db.insert(TABLE1, null, myContentValues);

        if (rowID == -1)
            return false;
        else
            return true;
    }

    // This method is used to insert a new task into table 2. The method returns a boolean in order for us to check if
    // the task was successfully inserted into the table.
    public boolean insertIntoTable2(String task, String description, int requestCode, String time, String review, String grade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues myContentValues =  new ContentValues();

        myContentValues.put(TABLE2_COL1, task);
        myContentValues.put(TABLE2_COL2, description);
        myContentValues.put(TABLE2_COL3, requestCode);
        myContentValues.put(TABLE2_COL4, time);
        myContentValues.put(TABLE2_COL5, review);
        myContentValues.put(TABLE2_COL6, grade);

        // If the data passed in wasn't inserted in the db, then the insert method (below) returns "-1".
        // Otherwise, it returns the ID of the newly inserted row.
        long rowID = db.insert(TABLE2, null, myContentValues);

        if (rowID == -1)
            return false;
        else
            return true;
    }

    // This method is used to insert a new task into table 3. The method returns a boolean in order for us to check if
    // the task was successfully inserted into the table.
    public boolean insertIntoTable3(String task, String description, int requestCode, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues myContentValues =  new ContentValues();

        myContentValues.put(TABLE3_COL1, task);
        myContentValues.put(TABLE3_COL2, description);
        myContentValues.put(TABLE3_COL3, requestCode);
        myContentValues.put(TABLE3_COL4, time);

        // If the data passed in wasn't inserted in the db, then the insert method (below) returns "-1".
        // Otherwise, it returns the ID of the newly inserted row.
        long rowID = db.insert(TABLE3, null, myContentValues);

        if (rowID == -1)
            return false;
        else
            return true;
    }


    // This method returns ALL the current/active tasks.
    public Cursor getAllCurrentTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE1, null);
        return res;
    }

    // This method returns ALL the current/active tasks.
    public Cursor getAllExpiredTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE2, null);
        return res;
    }


    // This method returns ALL the information of a current/active task.
    public Cursor getCurrentTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE1 + " where " + TABLE1_COL1 + " = \"" + task + "\"", null);
        return res;
    }

    // This method returns ALL the information of an expired task.
    public Cursor getExpiredTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE2 + " where " + TABLE2_COL1 + " = \"" + task + "\"", null);
        return res;
    }


    // This method returns ALL the current/active tasks.
    public Cursor getItemsFromTable3() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE3, null);
        return res;
    }

    // This method returns ALL the information of a task in table 3.
    public Cursor getTaskFromTable3(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE3 + " where " + TABLE2_COL1 + " = \"" + task + "\"", null);
        return res;
    }


    // -------------------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------------------


    // This method deletes a task/entry from table 1.
    public Integer deleteFromTable1(String task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE1, "Tasks = ?", new String[] {task});
    }

    // This method clears table 1.
    public Integer clearTable1() {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE1, null, null);
    }

    // ------------------------------------------------------------

    // This method clears table 2.
    public Integer clearTable2() {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE2, null, null);
    }

    // -------------------------------------------

    // This method deletes a task/entry from table 3.
    public Integer deleteFromTable3(String task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE3, "Tasks = ?", new String[] {task});
    }

    // This method clears table 3.
    public Integer clearTable3() {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE3, null, null);
    }
}
