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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpiredTaskFragment extends Fragment {
    static ListView expiredTasks_ListView;
    static ArrayList<String> expiredTasks_Array = new ArrayList<String>();
    //static HashMap<String, String> expiredTasks_Map = new HashMap<String, String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_expiredtasks, container, false);

        populateArrayWithTasks();

        expiredTasks_ListView = (ListView) myView.findViewById(R.id.expiredTasks_LV);
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, expiredTasks_Array);
        ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter);

        expiredTasks_ListView.setAdapter(myArrayAdapter);

        expiredTasks_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String taskName = parent.getItemAtPosition(position).toString();

                Intent myIntent = new Intent(getActivity(), TaskInfoActivity.class);
                myIntent.putExtra("myKey", taskName);
                startActivity(myIntent);
            }
        });

        return myView;
    }

    private void populateArrayWithTasks() {
        DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
        Cursor res = iGROW_db.getExpiredTasks();

        // By this point, the "expiredTasks_Array" is empty AND there are expired tasks stored in the database.
        if (expiredTasks_Array.size() == 0) {
            while (res.moveToNext())
                expiredTasks_Array.add(res.getString(0));
        }
        else
            return;
    }
}
