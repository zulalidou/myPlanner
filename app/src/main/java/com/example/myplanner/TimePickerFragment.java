package com.example.myplanner;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private TextView timeTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            // When the user has set the time, those values will be sent directly to the main activity
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                myCalendar.set(Calendar.SECOND, 0);

                String timeText = "Time: ";
                timeText += java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(myCalendar.getTime());
                timeTextView = (TextView) getActivity().findViewById(R.id.time_TV);
                //timeTextView.setText("Time: " + hourOfDay % 12 + ": " + minute);
                timeTextView.setText(timeText);

                setAlarm(myCalendar);
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }

    private void setAlarm(Calendar myCalendar) {
        AlarmManager myAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(getActivity(), 1, myIntent, 0);

        myAlarmManager.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), myPendingIntent);
    }
}
