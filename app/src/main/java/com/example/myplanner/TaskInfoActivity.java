package com.example.myplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/******************************************************************************************************************************
 The purpose of this class is to display the information about each task, whether current/active or expired.
 *****************************************************************************************************************************/

public class TaskInfoActivity extends AppCompatActivity {
    private ImageView image;
    private TextView taskName, taskDescription, taskTime;

    private TextView taskReview_header, taskReview_answer;
    private TextView taskGrade_header, taskGrade_answer;

    private View v1, v2;

    private Button deleteTask;

    private String task;

    DatabaseHandler myPlanner_DB;
    Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        image = (ImageView) findViewById(R.id.img_view);
        taskName = (TextView) findViewById(R.id.taskName_TV_answer);
        taskDescription = (TextView) findViewById(R.id.taskDescription_ET_answer);
        taskTime = (TextView) findViewById(R.id.taskTime_ET_answer);

        taskReview_header = (TextView) findViewById(R.id.taskReview_TV);
        taskReview_answer = (TextView) findViewById(R.id.taskReview_TV_answer);
        taskGrade_header = (TextView) findViewById(R.id.taskGrade_TV);
        taskGrade_answer = (TextView) findViewById(R.id.taskGrade_TV_answer);

        v1 = (View) findViewById(R.id.divider4);
        v2 = (View) findViewById(R.id.divider5);

        deleteTask = (Button) findViewById(R.id.deleteTask_BTN);


        if (getIntent().getStringExtra("fragmentName").equals("currentTasks"))
            currentTaskInfo();
        else
            expiredTaskInfo();
    }

    // This method displays the information of a current/active task
    private void currentTaskInfo() {
        image.setImageResource(R.mipmap.ic_in_progress_foreground);

        taskReview_header.setVisibility(View.GONE);
        taskReview_answer.setVisibility(View.GONE);
        taskGrade_header.setVisibility(View.GONE);
        taskGrade_answer.setVisibility(View.GONE);
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);

        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarmSet();
                finish();
            }
        });


        task = getIntent().getStringExtra("myKey");
        taskName.setText(task);

        myPlanner_DB = new DatabaseHandler(this);
        res = myPlanner_DB.getCurrentTask(task);

        res.moveToNext();
        String description = res.getString(1);
        String time = res.getString(3);

        taskDescription.setText(description);
        taskTime.setText(time.substring(6));
    }

    // This method displays the information of an expired task
    private void expiredTaskInfo() {
        image.setImageResource(R.mipmap.ic_complete_foreground);

        deleteTask.setVisibility(View.GONE);


        task = getIntent().getStringExtra("myKey");
        taskName.setText(task);

        myPlanner_DB = new DatabaseHandler(this);
        res = myPlanner_DB.getExpiredTask(task);

        res.moveToNext();

        taskDescription.setText(res.getString(1));
        taskTime.setText(res.getString(3).substring(6));
        taskReview_answer.setText(res.getString(4));
        taskGrade_answer.setText(res.getString(5));
    }

    // This cancels the alarm set for a current/active task, and deletes it from the database
    private void deleteAlarmSet() {
        AlarmManager myAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("task_name", task);

        int requestCode = res.getInt(2);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myAlarmManager.cancel(myPendingIntent);


        CurrentTaskFragment.currentTasks_Array.remove(task);
        myPlanner_DB.deleteFromTable1(task);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
        CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);

        if (CurrentTaskFragment.currentTasks_Array.size() == 0)
            CurrentTaskFragment.checkBox_img.setImageResource(R.mipmap.ic_checkmark_foreground);
    }
}
