package com.example.myplanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CurrentTaskFragment extends Fragment {
    static ListView currentTasks_ListView;
    static ArrayList<String> currentTasks_Array = new ArrayList<String>();  // Needed to populate the listview
    static ImageView checkBox;

    private FloatingActionButton addTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_currenttasks, container, false);

        MainActivity.toolbar.setTitle("Current tasks");


        retrieveTasksFromDB();

        currentTasks_ListView = (ListView) myView.findViewById(R.id.currentTasks_LV);
        checkBox = (ImageView) myView.findViewById(R.id.checkbox_current_IV);



        if (currentTasks_Array.size() >= 1) {

            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, currentTasks_Array);
            currentTasks_ListView.setAdapter(myArrayAdapter);

            currentTasks_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String taskName = parent.getItemAtPosition(position).toString();

                    Intent myIntent = new Intent(getActivity(), TaskInfoActivity.class);
                    myIntent.putExtra("fragmentName", "currentTasks");
                    myIntent.putExtra("myKey", taskName);
                    startActivity(myIntent);
                }
            });
        }

        else {
            checkBox.setImageResource(R.drawable.ic_check_box);
        }



        addTask = (FloatingActionButton) myView.findViewById(R.id.addTasks_FB);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });

        return myView;
    }

    private void retrieveTasksFromDB() {
        DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
        Cursor res = iGROW_db.getAllCurrentTasks();

        // The database isn't storing any active tasks
        if (res.getCount() == 0)
            return;
        else {
            if (currentTasks_Array.size() >= 1)
                return;

            // We populate the map, & check whether it's ok to also populate the array
            while (res.moveToNext())
                currentTasks_Array.add(res.getString(0));
        }
    }

    private void createTask() {
        FragmentManager myFragMan = getFragmentManager();
        FragmentTransaction myFragTrans = myFragMan.beginTransaction();

        CreateTaskFragment newFragment = new CreateTaskFragment();
        myFragTrans.replace(R.id.fragment_container, newFragment);
        myFragTrans.addToBackStack(null);   // Stack used in case we want to return to a previous fragment; null = no name for the fragment
        myFragTrans.commit();
    }
}
