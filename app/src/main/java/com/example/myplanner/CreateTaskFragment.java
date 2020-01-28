package com.example.myplanner;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;


public class CreateTaskFragment extends Fragment {
    private TextView taskTextView, descriptionTextView, timeTextView;
    public static EditText taskEditText, descriptionEditText;
    private Button setTimeBtn, saveNewTaskBtn, asdf, delete;

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

        taskTextView = (TextView) myView.findViewById(R.id.shortTaskDesc_TV);
        taskEditText = (EditText) myView.findViewById(R.id.shortTaskDesc_ET);
        descriptionTextView = (TextView) myView.findViewById(R.id.longTaskDesc_TV);
        descriptionEditText = (EditText) myView.findViewById(R.id.longTaskDesc_ET);
        timeTextView = (TextView) myView.findViewById(R.id.time_TV);
        setTimeBtn = (Button) myView.findViewById(R.id.setTime_BTN);
        saveNewTaskBtn = (Button) myView.findViewById(R.id.saveNewTask_BTN);

        asdf = (Button) myView.findViewById(R.id.asdf_BTN);


        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        saveNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewTask();
            }
        });

        asdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asdf_func();
            }
        });

        return myView;
    }

    private void setTime() {
        int currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = myCalendar.get(Calendar.MINUTE);

        myTimePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                myCalendar.set(Calendar.SECOND, 0);

                String timeText = "Time: ";
                timeText += java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(myCalendar.getTime());
                timeTextView = (TextView) getActivity().findViewById(R.id.time_TV);
                timeTextView.setText(timeText);

                setAlarm();
            }
        }, currentHour, currentMinute, android.text.format.DateFormat.is24HourFormat(getActivity()));

        myTimePickerDialog.show();
    }

    private void setAlarm() {
        myAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
        myIntent.putExtra("task_name", taskEditText.getText().toString());
        myPendingIntent = PendingIntent.getBroadcast(getActivity(), (int) System.currentTimeMillis(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void saveNewTask() {
        if (taskEditText.getText().toString().equals("") || descriptionEditText.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please fill in both the \"Task\" and \"Description\" entries", Toast.LENGTH_LONG).show();
        }
        else if (myTimePickerDialog == null){
            Toast.makeText(getContext(), "Please select a time", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "New task added", Toast.LENGTH_LONG).show();

            CurrentTaskFragment.currentTasks_Map.put(taskEditText.getText().toString(), descriptionEditText.getText().toString());
            CurrentTaskFragment.currentTasks_Array.add(taskEditText.getText().toString());
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
            CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter);

            boolean dataInserted = iGROW_db.insertIntoTable1(taskEditText.getText().toString(), timeTextView.getText().toString().substring(6));


            if (dataInserted)
                Toast.makeText(getContext(), "Data inserted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Data NOT inserted", Toast.LENGTH_SHORT).show();



            getActivity().getSupportFragmentManager().popBackStackImmediate();

            if (myAlarmManager != null)
                myAlarmManager.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), myPendingIntent); // the alarm gets fired at the time that the calendar was set above
        }
    }

    private void asdf_func() {
        Cursor res = iGROW_db.getCurrentTasks();

        if (res.getCount() == 0) {
            showMessage("Error", "Nothing found");
            return;
        }

        StringBuffer myBuffer = new StringBuffer();

        while(res.moveToNext()) {
            myBuffer.append("task : " + res.getString(0) + "\n");
            myBuffer.append("time : " + res.getString(1) + "\n\n");
        }

        showMessage("Data", myBuffer.toString());
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getContext());
        myBuilder.setCancelable(true);
        myBuilder.setTitle(title);
        myBuilder.setMessage(message);
        myBuilder.show();
    }
}
