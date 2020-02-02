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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpiredTaskFragment extends Fragment {
    static ListView expiredTasks_ListView;
    static ArrayList<String> expiredTasks_Array = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_expiredtasks, container, false);

        retrieveTasksFromDB();

        expiredTasks_ListView = (ListView) myView.findViewById(R.id.expiredTasks_LV);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, expiredTasks_Array);
        ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter);

        expiredTasks_ListView.setAdapter(myArrayAdapter);

        /*
        expiredTasks_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String taskName = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), taskName, Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(getActivity(), TaskInfoActivity.class);
                myIntent.putExtra("myKey", taskName);
                startActivity(myIntent);
            }
        });
         */

        return myView;
    }

    private void retrieveTasksFromDB() {
        expiredTasks_Array.clear();

        DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
        Cursor res = iGROW_db.getAllExpiredTasks();

        while (res.moveToNext())
            expiredTasks_Array.add(res.getString(0));
    }
}
