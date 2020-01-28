package com.example.myplanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// This class is designed to handle the Database file.
// - It extends the "SQLiteOpenHelper" class because the latter is the main class used for creating and managing our SQLite database.

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "iGROW.db";

    // This 1st table is used for storing the current tasks being performed, and the time the tasks expire.
    public static final String TABLE1 = "Current_Tasks_Table";
    public static final String TABLE1_COL1 = "Tasks";
    public static final String TABLE1_COL2 = "Time";

    // This 2nd table is used for storing the name of the expired tasks, their expiration time, a brief review on the completion of the
    // task, and a letter grade signifying the performance.
    public static final String TABLE2 = "Expired_Tasks_Table";
    public static final String TABLE2_COL1 = "Tasks";
    public static final String TABLE2_COL2 = "Time";
    public static final String TABLE2_COL3 = "Review";
    public static final String TABLE2_COL4 = "Grade";


    // The database for this app gets created when this constructor gets called.
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2); // This line creates the database.
    }

    // When the "onCreate" method gets called, the tables for the database gets created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE1 + " (Tasks TEXT, Time TEXT)");
        db.execSQL("create table " + TABLE2 + " (Tasks TEXT, Time TEXT, Review TEXT, Grade TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE1);
        db.execSQL("drop table if exists " + TABLE2);
        onCreate(db);
    }

    public boolean insertIntoTable1(String task, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues myContentValues =  new ContentValues();

        myContentValues.put(TABLE1_COL1, task);
        myContentValues.put(TABLE1_COL2, time);

        // If the data passed in wasn't inserted in the db, then the insert method (below) returns "-1".
        // Otherwise, it returns the ID of the newly inserted row.
        long rowID = db.insert(TABLE1, null, myContentValues);

        if (rowID == -1)
            return false;
        else
            return true;
    }

    public boolean insertIntoTable2(String task, String time, String review, String grade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues myContentValues =  new ContentValues();

        myContentValues.put(TABLE2_COL1, task);
        myContentValues.put(TABLE2_COL2, time);
        myContentValues.put(TABLE2_COL3, review);
        myContentValues.put(TABLE2_COL4, grade);


        // If the data passed in wasn't inserted in the db, then the insert method (below) returns "-1".
        // Otherwise, it returns the ID of the newly inserted row.
        long rowID = db.insert(TABLE2, null, myContentValues);

        if (rowID == -1)
            return false;
        else
            return true;
    }

    public Cursor getCurrentTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE1, null);

        return res;
    }

    public Cursor getExpiredTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE2, null);

        return res;
    }

    public Integer deleteFromTable1(String task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // The delete method below returns the number of rows affected if a whereClause is passed in.
        // If no data is deleted, 0 is returned.
        // The question mark in the whereClause gets replaced by the value(s) passed as the 3rd argument in the delete method.
        return db.delete(TABLE1, "Tasks = ?", new String[] {task});
    }
}
