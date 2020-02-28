package com.example.myplanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/****************************************************************************************************************************
 The purpose of this fragment class is to show a list of all the current/active tasks that are being performed.
 ***************************************************************************************************************************/

public class CurrentTaskFragment extends Fragment {
    static ImageView checkBox_img;

    static ListView currentTasks_ListView;
    static ArrayList<String> currentTasks_Array = new ArrayList<String>();

    private FloatingActionButton addTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.toolbar.setTitle("Current tasks");

        View myView = inflater.inflate(R.layout.fragment_currenttasks, container, false);


        checkBox_img = (ImageView) myView.findViewById(R.id.checkbox_current_IV);
        currentTasks_ListView = (ListView) myView.findViewById(R.id.currentTasks_LV);
        addTask = (FloatingActionButton) myView.findViewById(R.id.addTasks_FB);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });


        retrieveTasksFromDB();
        placeTasksInList(myView);


        return myView;
    }

    // This method opens the "createTask" fragment in order to let the users create a new task
    private void createTask() {
        FragmentManager myFragMan = getFragmentManager();
        FragmentTransaction myFragTrans = myFragMan.beginTransaction();

        CreateTaskFragment newFragment = new CreateTaskFragment();
        myFragTrans.replace(R.id.fragment_container, newFragment);
        myFragTrans.addToBackStack(null);   // Stack used in case we want to return to a previous fragment; null = no name for the fragment
        myFragTrans.commit();
    }

    // This method retrieves the current/active tasks from the database
    private void retrieveTasksFromDB() {
        DatabaseHandler myPlanner_DB = new DatabaseHandler(getContext());
        Cursor res = myPlanner_DB.getAllCurrentTasks();

        // The database isn't storing any active tasks
        if (res.getCount() == 0)
            return;
        else {
            // All the current/active tasks are already stored in the array, so there's no need to retrieve them again
            // from the database
            if (currentTasks_Array.size() >= 1)
                return;

            while (res.moveToNext())
                currentTasks_Array.add(res.getString(0));
        }
    }

    // This method either displays the tasks that are currently being performed, or it shows a checkmark image
    // to let the users know that there are no current/active tasks
    private void placeTasksInList(View myView) {
        if (currentTasks_Array.size() >= 1) {

            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, currentTasks_Array);
            currentTasks_ListView.setAdapter(myArrayAdapter);

            currentTasks_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String task = parent.getItemAtPosition(position).toString();

                    Intent myIntent = new Intent(getActivity(), TaskInfoActivity.class);
                    myIntent.putExtra("fragmentName", "currentTasks");
                    myIntent.putExtra("myKey", task);
                    startActivity(myIntent);
                }
            });
        }
        else
            checkBox_img.setImageResource(R.mipmap.ic_checkmark_foreground);
    }
}
