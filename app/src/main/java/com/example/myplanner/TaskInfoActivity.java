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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TaskInfoActivity extends AppCompatActivity {
    //private Toolbar toolbar;
    private TextView taskName;
    private TextView taskDescription;
    private TextView taskTime;
    private Button deleteTask;

    private String task;

    DatabaseHandler iGROW_db;
    Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);


        taskName = (TextView) findViewById(R.id.taskName_TV_answer);
        taskDescription = (TextView) findViewById(R.id.taskDescription_ET_answer);
        taskTime = (TextView) findViewById(R.id.taskTime_ET_answer);


        if (getIntent().getStringExtra("fragmentName") == "currentTasks") {
            deleteTask = (Button) findViewById(R.id.deleteTask_BTN);

            task = getIntent().getStringExtra("myKey");
            taskName.setText(task);

            iGROW_db = new DatabaseHandler(this);
            res = iGROW_db.getCurrentTask(task);

            res.moveToNext();
            String description = res.getString(1);
            String time = res.getString(3);


            taskDescription.setText(description);
            taskTime.setText(time.substring(6));




            deleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAlarmSet();
                    finish();
                }
            });
        }
        else {
            deleteTask = (Button) findViewById(R.id.deleteTask_BTN);
            deleteTask.setVisibility(View.GONE);


            task = getIntent().getStringExtra("myKey");
            taskName.setText(task);

            iGROW_db = new DatabaseHandler(this);
            res = iGROW_db.getExpiredTask(  task);

            res.moveToNext();
            String description = res.getString(1);
            String time = res.getString(3);


            taskDescription.setText(description);
            taskTime.setText(time.substring(6));
        }









/*
        if (CurrentTaskFragment.currentTasks_Array.contains(task)) {
            taskDescription.setText(description);
        }
        else {
            taskDescription.setText(description);
        }

 */



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



        // =================================

        if (CurrentTaskFragment.currentTasks_Array.size() == 0) {
            //CurrentTaskFragment.checkBox = (ImageView) findViewById(R.id.checkbox_IV);
            CurrentTaskFragment.checkBox.setImageResource(R.drawable.ic_check_box);
            //Toast.makeText(MainActivity.class, "2nd option", Toast.LENGTH_LONG).show();
        }

/*
        CurrentTaskFragment.currentTasks_Array.remove(task);
        CurrentTaskFragment.currentTasks_Map.remove(task);

        // update task screen
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(TaskInfoActivity.this, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
        CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);
 */
    }
}
