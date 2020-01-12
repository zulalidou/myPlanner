package com.example.myplanner;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    private static NotificationManager myNotificationManager;

    // This method is called when the alarm gets fired.. so we want to show the notification when it gets fired
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.putExtra("goto", "expiredTasks fragment");
        PendingIntent myPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), myIntent, 0);

        String task = intent.getStringExtra("task_name");

        buildNotification(context, task, myPendingIntent);
        removeTaskFromCurrentList(context, task);
        sendTaskToExpiredList(context, task);
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

    private void removeTaskFromCurrentList (Context context, String task) {
        CurrentTaskFragment.currentTasks_Array.remove(task);
        CurrentTaskFragment.currentTasks_Map.remove(task);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
        CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);
    }

    private void sendTaskToExpiredList (Context context, String task) {
        ExpiredTaskFragment.expiredTasks_Array.add(task);
    }
}
