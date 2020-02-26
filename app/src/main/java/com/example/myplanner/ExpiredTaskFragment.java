package com.example.myplanner;

import android.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ExpiredTaskFragment extends Fragment {
    static ListView expiredTasks_ListView;
    static ArrayList<String> expiredTasks_Array = new ArrayList<String>();
    static ImageView checkBox;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_expiredtasks, container, false);

        MainActivity.toolbar.setTitle("Expired tasks");

        retrieveTasksFromDB();

        expiredTasks_ListView = (ListView) myView.findViewById(R.id.expiredTasks_LV);
        checkBox = (ImageView) myView.findViewById(R.id.checkbox_expired_IV);


        DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
        Cursor res = iGROW_db.getItemsFromTable3();
        Toast.makeText(getContext(), Boolean.toString(res.moveToFirst()), Toast.LENGTH_LONG).show();


        // if true, it means a task just expired
        if (res.moveToFirst()) {
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, expiredTasks_Array);
            ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter);
            expiredTasks_ListView.setAdapter(myArrayAdapter);

            // The line below is necessary because it makes sure that the next time this (expiredTaskFragment) fragment gets opened, it doesn't
            // show the review dialog box; the review dialog box is ONLY shown after the user clicks on the notification that informs them that
            // the time allocated to accomplishing their task has come
            if (getActivity().getIntent().getStringExtra("A task has just expired") != null)
                getActivity().getIntent().removeExtra("A task has just expired");


            do {
                String task = res.getString(0);

                ReviewDialog myReviewDialog = new ReviewDialog(task);
                myReviewDialog.show(getFragmentManager(), "reviewDialog");
            } while (res.moveToNext());

        }
        // No active task expired recently. The users have just decided to open the mode that contains the expired tasks
        else if(expiredTasks_Array.size() >= 1) {
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, expiredTasks_Array);
            ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter);
            expiredTasks_ListView.setAdapter(myArrayAdapter);

            /*
            expiredTasks_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String taskName = parent.getItemAtPosition(position).toString();

                    Intent myIntent = new Intent(getActivity(), TaskInfoActivity.class);
                    myIntent.putExtra("myKey", taskName);
                    startActivity(myIntent);
                }
            });

             */

            expiredTasks_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String taskName = parent.getItemAtPosition(position).toString();

                    Intent myIntent = new Intent(getActivity(), TaskInfoActivity.class);
                    myIntent.putExtra("fragmentName", "expiredTasks");
                    myIntent.putExtra("myKey", taskName);
                    startActivity(myIntent);
                }
            });

        }
        else {
            checkBox.setImageResource(R.drawable.ic_check_box);
        }


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
