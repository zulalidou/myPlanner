package com.example.myplanner;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
        Calendar myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            // When the user has set the time, those values will be sent directly to the main activity
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeTextView = (TextView) getActivity().findViewById(R.id.time_TV);
                timeTextView.setText("Time: " + hourOfDay % 12 + ": " + minute);
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }
}
