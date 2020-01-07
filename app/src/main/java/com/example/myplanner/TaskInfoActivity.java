package com.example.myplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TaskInfoActivity extends AppCompatActivity {
    //private Toolbar toolbar;
    private TextView taskName;
    private EditText taskDescription;
    private Button deleteTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);


        taskName = (TextView) findViewById(R.id.taskName_TV_answer);
        taskDescription = (EditText) findViewById(R.id.taskDescription_ET_answer);
        deleteTask = (Button) findViewById(R.id.deleteTask_BTN);

        final String key = getIntent().getStringExtra("myKey");
        taskName.setText(key);

        final String value = CurrentTaskFragment.tasks_Map.get(key);
        taskDescription.setText(value);


        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentTaskFragment.taskArray.remove(key);
                CurrentTaskFragment.tasks_Map.remove(key);

                // update task screen
                ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(TaskInfoActivity.this, android.R.layout.simple_list_item_1, CurrentTaskFragment.taskArray);
                CurrentTaskFragment.taskListView.setAdapter(myArrayAdapter);
                finish();
            }
        });
    }
}
