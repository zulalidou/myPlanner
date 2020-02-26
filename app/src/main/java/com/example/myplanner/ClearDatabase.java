package com.example.myplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ClearDatabase extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHandler myDB = new DatabaseHandler(context);

        myDB.clearTable1();
        myDB.clearTable2();
        myDB.clearTable3();

        Toast.makeText(context, "tables cleared!", Toast.LENGTH_LONG).show();
    }
}
