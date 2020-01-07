package com.example.myplanner;

import android.app.TimePickerDialog;
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
    private EditText taskEditText, descriptionEditText;
    private Button setTimeBtn, saveNewTaskBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_createtask, container, false);

        taskTextView = (TextView) myView.findViewById(R.id.shortTaskDesc_TV);
        taskEditText = (EditText) myView.findViewById(R.id.shortTaskDesc_ET);
        descriptionTextView = (TextView) myView.findViewById(R.id.longTaskDesc_TV);
        descriptionEditText = (EditText) myView.findViewById(R.id.longTaskDesc_ET);
        timeTextView = (TextView) myView.findViewById(R.id.time_TV);
        setTimeBtn = (Button) myView.findViewById(R.id.setTime_BTN);
        saveNewTaskBtn = (Button) myView.findViewById(R.id.saveNewTask_BTN);

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

        return myView;
    }

    private void setTime() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), "time picker"); // "time picker" is just a tag
    }

    private void saveNewTask() {
        if (taskEditText.getText().toString().equals("") || descriptionEditText.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please fill in both the \"Task\" and \"Description\" entries", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "New task added", Toast.LENGTH_LONG).show();

            CurrentTaskFragment.tasks_Map.put(taskEditText.getText().toString(), descriptionEditText.getText().toString());
            CurrentTaskFragment.taskArray.add(taskEditText.getText().toString());
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, CurrentTaskFragment.taskArray);
            CurrentTaskFragment.taskListView.setAdapter(myArrayAdapter);

            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
