package com.example.myplanner;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Date;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    private static NotificationManager myNotificationManager;

    // This method is called when the alarm gets fired.. so we want to show the notification when it gets fired
    @Override
    public void onReceive(Context context, Intent intent) {
        String task = intent.getStringExtra("task_name");

        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.putExtra("A task has just expired", task);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK); // Forces 'FLAG_ACTIVITY_NEW_TASK' to start a new task

        PendingIntent myPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), myIntent, 0);


        buildNotification(context, task, myPendingIntent);
        updateDatabase(context, task);
    }

    private void buildNotification(Context context, String task, PendingIntent myPendingIntent) {
        long[] vibrate = { 0, 100, 200, 300 };

        NotificationCompat.Builder newNotification = new NotificationCompat.Builder(context, "channel_ID") // the 2nd paramenter ("channel_id") is ignored for Android versions lower than 8.0 (Oreo)
                .setContentTitle(task)
                .setContentText("Task " + task + " has now expired")
                .setSmallIcon(R.drawable.ic_star)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true) // notification goes away (from status bar) once clicked
                .setVibrate(vibrate)
                .setContentIntent(myPendingIntent);

        getManager(context).notify((int) System.currentTimeMillis(), newNotification.build());
    }

    private static NotificationManager getManager(Context context) {
        if (myNotificationManager == null)
            myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        return myNotificationManager;
    }

    private void updateDatabase (Context context, String task) {
        DatabaseHandler iGROW_db = new DatabaseHandler(context);
        Cursor res = iGROW_db.getCurrentTask(task);

        res.moveToNext();
        String description = res.getString(1);
        int requestCode = res.getInt(2);
        String time = res.getString(3);

        iGROW_db.insertIntoTable3(task, description, requestCode, time);


        /*
        DatabaseHandler iGROW_db = new DatabaseHandler(context);
        Cursor res = iGROW_db.getCurrentTask(task);

        Log.d("TAG", "res = " + res);

        res.moveToNext();
        String description = res.getString(1);
        int requestCode = res.getInt(2);
        String time = res.getString(3);


        // Adds the task to "Expired_Tasks_Table"
        iGROW_db.insertIntoTable2(task, description, requestCode, time, "Did ok i guess :/", "B");
        ExpiredTaskFragment.expiredTasks_Array.add(task);

        // Removes the task from "Current_Tasks_Table"
        iGROW_db.deleteFromTable1(task);
        CurrentTaskFragment.currentTasks_Array.remove(task);



        if (CurrentTaskFragment.currentTasks_ListView != null) {
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
            CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);
        }

 */

        //Log.d("AR => ", "updateDatabase");
        // CurrentTaskFragment.currentTasks_ListView == null when we complete
    }
}
