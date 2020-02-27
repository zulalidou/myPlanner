package com.example.myplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    private ImageView image;
    private TextView taskName;
    private TextView taskDescription;
    private TextView taskTime;

    private TextView taskReview_header;
    private TextView taskReview;
    private TextView taskGrade_header;
    private TextView taskGrade;

    private View v1;
    private View v2;

    private Button deleteTask;

    private String task;

    DatabaseHandler iGROW_db;
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
        taskReview = (TextView) findViewById(R.id.taskReview_TV_answer);
        taskGrade_header = (TextView) findViewById(R.id.taskGrade_TV);
        taskGrade = (TextView) findViewById(R.id.taskGrade_TV_answer);

        v1 = (View) findViewById(R.id.divider4);
        v2 = (View) findViewById(R.id.divider5);

        deleteTask = (Button) findViewById(R.id.deleteTask_BTN);



        if (getIntent().getStringExtra("fragmentName").equals("currentTasks")) {
            image.setImageResource(R.mipmap.ic_in_progress_foreground);

            taskReview_header.setVisibility(View.GONE);
            taskGrade_header.setVisibility(View.GONE);

            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);

            taskReview.setVisibility(View.GONE);
            taskGrade.setVisibility(View.GONE);



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
            image.setImageResource(R.mipmap.ic_complete_foreground);

            deleteTask.setVisibility(View.GONE);


            task = getIntent().getStringExtra("myKey");
            taskName.setText(task);

            iGROW_db = new DatabaseHandler(this);
            res = iGROW_db.getExpiredTask(  task);

            res.moveToNext();

            taskDescription.setText(res.getString(1));
            taskTime.setText(res.getString(3).substring(6));
            taskReview.setText(res.getString(4));
            taskGrade.setText(res.getString(5));
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
            CurrentTaskFragment.checkBox.setImageResource(R.mipmap.ic_checkmark_foreground);
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
