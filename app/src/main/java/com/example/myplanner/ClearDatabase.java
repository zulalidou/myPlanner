package com.example.myplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

/****************************************************************************************************************************
 Every day at 11:59:59pm, all the tasks (whether current or expired) are completely cleared from the database. When the alarm
 for this action goes off, it calls this receiver class, which clears every single entry in all the tables in the database.
 ***************************************************************************************************************************/

public class ClearDatabase extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        clearTables(context);
        updateLists(context);
    }

    // Clears the tables in the database
    private void clearTables(Context context) {
        DatabaseHandler myPlanner_DB = new DatabaseHandler(context);

        myPlanner_DB.clearTable1();
        myPlanner_DB.clearTable2();
        myPlanner_DB.clearTable3();
    }

    // Clears the two lists that show the current and expired tasks
    private void updateLists(Context context) {
        if (CurrentTaskFragment.currentTasks_ListView != null) {
            CurrentTaskFragment.currentTasks_Array.clear();
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
            CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);
        }

        if (ExpiredTaskFragment.expiredTasks_ListView != null) {
            ExpiredTaskFragment.expiredTasks_Array.clear();
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, ExpiredTaskFragment.expiredTasks_Array);
            ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter);
        }
    }
}
