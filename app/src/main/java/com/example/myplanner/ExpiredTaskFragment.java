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

import java.util.ArrayList;

/****************************************************************************************************************************
 The purpose of this fragment class is to show a list of all the tasks that have expired.
 ***************************************************************************************************************************/

public class ExpiredTaskFragment extends Fragment {
    static ImageView checkBox_img;

    static ListView expiredTasks_ListView;
    static ArrayList<String> expiredTasks_Array = new ArrayList<String>();

    static ArrayList<String> tasksAboutToBeReviewed = new ArrayList<String>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.toolbar.setTitle("Expired tasks");

        View myView = inflater.inflate(R.layout.fragment_expiredtasks, container, false);


        checkBox_img = (ImageView) myView.findViewById(R.id.checkbox_expired_IV);
        expiredTasks_ListView = (ListView) myView.findViewById(R.id.expiredTasks_LV);


        retrieveTasksFromDB();
        checkIfAnyTasksExpired();



        return myView;
    }

    // This method retrieves the expired tasks from the database
    private void retrieveTasksFromDB() {
        expiredTasks_Array.clear();

        DatabaseHandler myPlanner_DB = new DatabaseHandler(getContext());
        Cursor res = myPlanner_DB.getAllExpiredTasks();

        while (res.moveToNext())
            expiredTasks_Array.add(res.getString(0));
    }

    // This method either displays the tasks that have already expired, or it shows a checkmark image
    // to let the users know that there are no tasks that have expired
    private void checkIfAnyTasksExpired() {
        DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
        Cursor res = iGROW_db.getItemsFromTable3();

        // if true, it means at least one task has expired
        if (res.moveToFirst()) {
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, expiredTasks_Array);
            expiredTasks_ListView.setAdapter(myArrayAdapter);


            // The line below is necessary because it makes sure that the next time this (expiredTaskFragment) fragment gets opened, it doesn't
            // show the review dialog box; the review dialog box is ONLY shown after the user clicks on the notification that informs them that
            // the time allocated to accomplishing their task has come
            //if (getActivity().getIntent().getStringExtra("A task has just expired") != null)
            //    getActivity().getIntent().removeExtra("A task has just expired");
            //////
            //up//
            //////

            do {
                String task = res.getString(0);

                boolean taskFound = false;
                if (tasksAboutToBeReviewed.contains(task)) {
                    taskFound = true;
                    break;
                }

                if (taskFound)
                    continue;

                tasksAboutToBeReviewed.add(task);

                ReviewDialog myReviewDialog = new ReviewDialog(task);
                myReviewDialog.show(getFragmentManager(), "reviewDialog");
            } while (res.moveToNext());


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
        // No active task expired recently. The users have just decided to open the mode that contains the expired tasks
        else if(expiredTasks_Array.size() >= 1) {
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, expiredTasks_Array);
            expiredTasks_ListView.setAdapter(myArrayAdapter);

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
        else
            checkBox_img.setImageResource(R.mipmap.ic_checkmark_foreground);
    }
}
