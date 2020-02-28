package com.example.myplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

/*************************************************************************************************************************
 The purpose of this fragment class is to create new tasks.
 ************************************************************************************************************************/

public class CreateTaskFragment extends Fragment {
    private TextView task_header, description_header, time_header;
    private EditText task_answer, description_answer;

    private Button setTimeBtn, saveBtn, deleteBtn;

    private int requestCode;

    private TimePickerDialog myTimePickerDialog;
    private static final Calendar myCalendar = Calendar.getInstance();

    private AlarmManager myAlarmManager;
    private PendingIntent myPendingIntent;

    private DatabaseHandler iGROW_db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_createtask, container, false);
        iGROW_db = new DatabaseHandler(getContext());

        MainActivity.toolbar.setTitle("Create a task");

        task_header = (TextView) myView.findViewById(R.id.task_header);
        task_answer = (EditText) myView.findViewById(R.id.task_answer);
        description_header = (TextView) myView.findViewById(R.id.description_header);
        description_answer = (EditText) myView.findViewById(R.id.description_answer);
        time_header = (TextView) myView.findViewById(R.id.time_header);

        setTimeBtn = (Button) myView.findViewById(R.id.setTime_BTN);
        saveBtn = (Button) myView.findViewById(R.id.save_BTN);
        deleteBtn = (Button) myView.findViewById(R.id.delete_BTN);


        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewTask();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // closes the "createTask" fragment immediately after the user creates the task
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        return myView;
    }

    // This method helps the users set the time they'd like to have accomplished a certain task
    private void setTime() {
        int currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = myCalendar.get(Calendar.MINUTE);

        myTimePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                myCalendar.set(Calendar.SECOND, 0);

                String time = "Time: ";
                time += java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(myCalendar.getTime());
                time_header = (TextView) getActivity().findViewById(R.id.time_header);
                time_header.setText(time);

                setAlarm();
            }
        }, currentHour, currentMinute, android.text.format.DateFormat.is24HourFormat(getActivity()));

        myTimePickerDialog.show();
    }

    // This method sets the alarm for the new task using the time specified by the user
    private void setAlarm() {
        myAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
        myIntent.putExtra("task_name", task_answer.getText().toString());
        //////
        //up//
        //////

        requestCode = (int) System.currentTimeMillis();
        myPendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // This method makes sure that all the fields have been filled out before saving the new task
    private void saveNewTask() {
        if (task_answer.getText().toString().equals("") || description_answer.getText().toString().equals(""))
            Toast.makeText(getContext(), "Please fill in both the \"Task\" and \"Description\" entries", Toast.LENGTH_LONG).show();
        else if (myTimePickerDialog == null)
            Toast.makeText(getContext(), "Please select a time", Toast.LENGTH_LONG).show();
        else {
            String task = task_answer.getText().toString();
            String description = description_answer.getText().toString();
            int taskRequestCode = requestCode;
            String time = time_header.getText().toString();

            CurrentTaskFragment.currentTasks_Array.add(task);
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
            CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);


            boolean inserted = iGROW_db.insertIntoTable1(task, description, taskRequestCode, time);

            Log.d("muh_tag", String.valueOf(inserted));


            if (inserted)
                Toast.makeText(getContext(), "INSERTED", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), "NOT INSERTED", Toast.LENGTH_LONG).show();



            // closes the "createTask" fragment immediately after the user creates the task
            getActivity().getSupportFragmentManager().popBackStackImmediate();


            // the alarm gets fired at the time that the calendar was set above
            if (myAlarmManager != null)
                myAlarmManager.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), myPendingIntent);
        }
    }
}
