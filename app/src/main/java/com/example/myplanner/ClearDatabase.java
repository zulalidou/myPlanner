package com.example.myplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ClearDatabase extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        clearTables(context);
        updateListviews(context);
    }

    private void clearTables(Context context) {
        DatabaseHandler myDB = new DatabaseHandler(context);

        myDB.clearTable1();
        myDB.clearTable2();
        myDB.clearTable3();

        Toast.makeText(context, "tables cleared!", Toast.LENGTH_LONG).show();
    }

    private void updateListviews(Context context) {
        if (CurrentTaskFragment.currentTasks_ListView != null) {
            CurrentTaskFragment.currentTasks_Array.clear();
            ArrayAdapter<String> myArrayAdapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
            CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter1);
        }

        if (ExpiredTaskFragment.expiredTasks_ListView != null) {
            ExpiredTaskFragment.expiredTasks_Array.clear();
            ArrayAdapter<String> myArrayAdapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, ExpiredTaskFragment.expiredTasks_Array);
            ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter2);
        }
    }
}
