package com.example.myplanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentTaskFragment extends Fragment {
    static ListView currentTasks_ListView;
    static ArrayList<String> currentTasks_Array = new ArrayList<String>();
    static HashMap<String, String> currentTasks_Map = new HashMap<String, String>();
    private FloatingActionButton addTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_currenttasks, container, false);

        populateArrayWithTasks();

        currentTasks_ListView = (ListView) myView.findViewById(R.id.currentTasks_LV);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, currentTasks_Array);
        currentTasks_ListView.setAdapter(myArrayAdapter);

        currentTasks_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String taskName = parent.getItemAtPosition(position).toString();

                Intent myIntent = new Intent(getActivity(), TaskInfoActivity.class);
                myIntent.putExtra("myKey", taskName);
                startActivity(myIntent);
            }
        });


        addTask = (FloatingActionButton) myView.findViewById(R.id.addTasks_FB);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });

        return myView;
    }

    private void populateArrayWithTasks() {
        DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
        Cursor res = iGROW_db.getCurrentTasks();

        // No current tasks saved in the database.
        if (res.getCount() == 0)
            return;
        else {
            // The "currentTasks_Array" already contains the tasks, so we don't need to re-populate it.
            if (currentTasks_Array.size() > 0)
                return;

            // By this point, the "currentTasks_Array" is empty AND there are active tasks stored in the database.
            while(res.moveToNext())
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
