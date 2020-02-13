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

        MainActivity.toolbar.setTitle("Expired tasks");
        expiredTasks_ListView = (ListView) myView.findViewById(R.id.expiredTasks_LV);

        retrieveTasksFromDB();

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, expiredTasks_Array);
        ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter);
        expiredTasks_ListView.setAdapter(myArrayAdapter);




        // The line below is necessary because it makes sure that the next time this (expiredTaskFragment) fragment gets opened, it doesn't
        // show the review dialog box; the review dialog box is ONLY shown after the user clicks on the notification that informs them that
        // the time allocated to accomplishing their task has come
        if (getActivity().getIntent().getStringExtra("A task has just expired") != null)
            getActivity().getIntent().removeExtra("A task has just expired");


        DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
        Cursor res = iGROW_db.getItemsFromTable3();

        while (res.moveToNext()) {
            Log.d("qwerty", "count = " + res.getCount());
            String task = res.getString(0);

            ReviewDialog myReviewDialog = new ReviewDialog(task);
            myReviewDialog.show(getFragmentManager(), "reviewDialog");
        }

/*
        if (getActivity().getIntent().getStringExtra("A task has just expired") != null) {
            String task = getActivity().getIntent().getStringExtra("A task has just expired");

            //res = iGROW_db.getExpiredTask(task);

            ReviewDialog myReviewDialog = new ReviewDialog(task);
            myReviewDialog.show(getFragmentManager(), "reviewDialog");


            // The line below is necessary because it makes sure that the next time this (expiredTaskFragment) fragment gets opened, it doesn't
            // show the review dialog box; the review dialog box is ONLY shown after the user clicks on the notification that informs them that
            // the time allocated to accomplishing their task has come
            getActivity().getIntent().removeExtra("A task has just expired");
        }

 */

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

    public void showMessage(String title, String message) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getContext());
        myBuilder.setCancelable(true);
        myBuilder.setTitle(title);
        myBuilder.setMessage(message);
        myBuilder.show();
    }
}
