package com.example.myplanner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/***************************************************************************************************************************
 The purpose of this receiver class is to create notifications and display them to the users whenever the time set for a
 notification expires, and to perform some kind of action if the notification gets clicked.
 - In this case, the action performed after clicking the notification is to insert the information about the task that just
   expired into table 3 (in the database).
 **************************************************************************************************************************/

public class AlarmReceiver extends BroadcastReceiver {
    private static NotificationManager myNotificationManager;

    // This method is called when the alarm set for the notification gets fired.. so we want to display the notifications to the users
    @Override
    public void onReceive(Context context, Intent intent) {
        String task = intent.getStringExtra("task_name");

        Intent myIntent = new Intent(context, MainActivity.class);
        //myIntent.putExtra("A task has just expired", task); // This might actually be unnecessary since I'm already inserting the task in table3
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK); // Forces 'FLAG_ACTIVITY_NEW_TASK' to start a new task

        PendingIntent myPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), myIntent, 0);

        buildNotification(context, task, myPendingIntent);
        saveTaskInTable3(context, task);
    }

    // Creates the notification to be displayed to the users
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

    // Saves the task that just expired in table 3 (in the database). Table 3 essentially represents all the tasks that have
    // recently expired AND need to be reviewed
    private void saveTaskInTable3 (Context context, String task) {
        DatabaseHandler myPlanner_DB = new DatabaseHandler(context);
        Cursor res = myPlanner_DB.getCurrentTask(task);

        Log.d("task = ", task);
        Log.d("res = ", String.valueOf(res));

        res.moveToNext();
        String description = res.getString(1);
        int requestCode = res.getInt(2);
        String time = res.getString(3);

        myPlanner_DB.insertIntoTable3(task, description, requestCode, time);
    }
}
