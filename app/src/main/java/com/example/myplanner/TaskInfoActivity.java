package com.example.myplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TaskInfoActivity extends AppCompatActivity {
    //private Toolbar toolbar;
    private TextView taskName;
    private EditText taskDescription;
    private Button deleteTask;

    private String task;

    DatabaseHandler iGROW_db;
    Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);


        taskName = (TextView) findViewById(R.id.taskName_TV_answer);
        taskDescription = (EditText) findViewById(R.id.taskDescription_ET_answer);
        deleteTask = (Button) findViewById(R.id.deleteTask_BTN);


        task = getIntent().getStringExtra("myKey");
        taskName.setText(task);

        iGROW_db = new DatabaseHandler(this);
        res = iGROW_db.getCurrentTask(task);

        res.moveToNext();
        String description = res.getString(1);



        if (CurrentTaskFragment.currentTasks_Array.contains(task)) {
            taskDescription.setText(description);
        }
        else {
            taskDescription.setText(description);
        }


        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarmSet();
                finish();
            }
        });
    }

    private void deleteAlarmSet() {
        AlarmManager myAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("task_name", task);

        int requestCode = res.getInt(2);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        myAlarmManager.cancel(myPendingIntent);


        CurrentTaskFragment.currentTasks_Array.remove(task);
        iGROW_db.deleteFromTable1(task);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
        CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);

/*
        CurrentTaskFragment.currentTasks_Array.remove(task);
        CurrentTaskFragment.currentTasks_Map.remove(task);

        // update task screen
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(TaskInfoActivity.this, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
        CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);
 */
    }
}
