package com.example.myplanner;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    // This method is called when the alarm gets fired.. so we want to show the notification when it gets fired
    @Override
    public void onReceive(Context context, Intent intent) {
        App qwerty = new App(context);
        NotificationCompat.Builder builder = qwerty.getChannelNotification();
        qwerty.getManager(context).notify(1, builder.build());


        /*
        App helper = new App(context);
        NotificationCompat.Builder builder = helper.getChannelNotification();
        helper.getManager().notify(1, builder.build());
        */

        /*
        NotificationManager MyNotifyManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder myNotifBuilder = new NotificationCompat.Builder(context, App.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Task Expired")
                .setContentText("This is a sample message :P")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

        MyNotifyManager.notify(1, myNotifBuilder.build());

        notificationHelper.getManager().notify(1, myNotifBuilder.build());
         */
    }
}
