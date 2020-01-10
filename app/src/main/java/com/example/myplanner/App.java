package com.example.myplanner;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

// "This class wraps the entire app we're building with all its components (like activities and services), and
// if we want to do a setup at the start of our application (and not for example in a particular activity), then
// this is the right place to do it."

public class App extends ContextWrapper {
    public static final String CHANNEL_NAME = "AlarmNotificationChannel";
    public static final String CHANNEL_ID = "Channel_1";
    private static NotificationManager myNotificationManager;
    private Context c = this;


    public App(Context base) {
        super(base);

        //createChannel();
        //getManager(c);
    }

/*
    private void createChannel() {
        NotificationChannel newChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        newChannel.enableLights(true);
        newChannel.enableVibration(true);
        newChannel.setDescription("This channel is for the alarm notifications.");
        
        getManager().createNotificationChannel(newChannel);
    }

 */

    public static NotificationManager getManager(Context c) {
        if (myNotificationManager == null)
            myNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        return myNotificationManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        long[] vibrate = { 0, 100, 200, 300 };

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Task Expired")
                .setContentText("This is a sample message :P")
                .setSmallIcon(R.drawable.ic_star)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVibrate(vibrate);
    }


    /***********************************************************************************************
    public static final String NOTIFICATION_CHANNEL_ID = "AlarmNotificationChannel";
    private NotificationManager notificationManager;


    // This "onCreate" method will be called right before we start any activities
    // - Used for setting up the notification channel
    @Override
    public void onCreate() {
        super.onCreate();


        createNotificationChannel();
    }

    public void createNotificationChannel() {
        // We first check if we're on Android Oreo or higher because the Notification channel class isn't
        // available on lower API levels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel myAlarmChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "AlarmNotificationChannel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            myAlarmChannel.setDescription("This channel is for the alarm notifications.");

            if (notificationManager == null)
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(myAlarmChannel);
        }
    }


    public NotificationManager getManager() {
        if (myManager == null)
            myManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        return myManager;
    }


    public NotificationCompat.Builder sendToChannel() {
        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Task Expired")
                .setContentText("This is a sample message :P")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

       // notificationManager.notify(1, myNotification);
        //return myNotification;
    }

     **********************************************************************************************/
}
