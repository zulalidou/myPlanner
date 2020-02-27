package com.example.myplanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

// This class is designed to handle the Database file.
// - It extends the "SQLiteOpenHelper" class because the latter is the main class used for creating and managing our SQLite database.

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "iGROW.db";

    // This 1st table is used for storing the current tasks being performed, and the time the tasks expire.
    public static final String TABLE1 = "Current_Tasks_Table";
    public static final String TABLE1_COL1 = "Tasks";
    public static final String TABLE1_COL2 = "Descriptions";
    public static final String TABLE1_COL3 = "Request_Codes";
    public static final String TABLE1_COL4 = "Time";

    // This 2nd table is used for storing the name of the expired tasks, their expiration time, a brief review on the completion of the
    // task, and a letter grade signifying the performance.
    public static final String TABLE2 = "Expired_Tasks_Table";
    public static final String TABLE2_COL1 = "Tasks";
    public static final String TABLE2_COL2 = "Descriptions";
    public static final String TABLE2_COL3 = "Request_Codes";
    public static final String TABLE2_COL4 = "Time";
    public static final String TABLE2_COL5 = "Review";
    public static final String TABLE2_COL6 = "Grade";


    public static final String TABLE3 = "ReviewDialog_Table";
    public static final String TABLE3_COL1 = "Tasks";
    public static final String TABLE3_COL2 = "Descriptions";
    public static final String TABLE3_COL3 = "Request_Codes";
    public static final String TABLE3_COL4 = "Time";

    // The database for this app gets created when this constructor gets called.
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 5); // This line creates the database.
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

    public Cursor getAllCurrentTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE1, null);
        return res;
    }

    public Cursor getCurrentTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE1 + " where " + TABLE1_COL1 + " = '" + task + "'", null);
        return res;
    }

    public Cursor getExpiredTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE2 + " where " + TABLE2_COL1 + " = '" + task + "'", null);
        return res;
    }

    public Cursor getAllExpiredTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE2, null);
        return res;
    }


    public Cursor getTaskFromTable3(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE3 + " where " + TABLE2_COL1 + " = '" + task + "'", null);
        return res;
    }

    public Cursor getItemsFromTable3() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE3, null);
        return res;
    }

    // ------------------------------------------------------------

    public Integer deleteFromTable1(String task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE1, "Tasks = ?", new String[] {task});
    }

    public Integer clearTable1() {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE1, null, null);
    }

    // ------------------------------------------------------------

    public Integer clearTable2() {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE2, null, null);
    }

    // -------------------------------------------

    public Integer deleteFromTable3(String task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE3, "Tasks = ?", new String[] {task});
    }

    public Integer clearTable3() {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE3, null, null);
    }
}
