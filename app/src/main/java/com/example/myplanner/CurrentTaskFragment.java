package com.example.myplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CurrentTaskFragment extends Fragment {
    static ListView taskList;
    private FloatingActionButton addTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_currenttasks, container, false);


        taskList = (ListView) myView.findViewById(R.id.tasks_LV);
        addTask = (FloatingActionButton) myView.findViewById(R.id.addTasks_FB);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });

        return myView;
    }

    private void createTask() {
        Fragment newFragment = new CreateTaskFragment();
        FragmentTransaction myTransaction = getFragmentManager().beginTransaction();
        myTransaction.replace(R.id.fragment_container, newFragment, "createTaskFragment_TAG");
        myTransaction.addToBackStack(null);
        myTransaction.commit();
    }
}
