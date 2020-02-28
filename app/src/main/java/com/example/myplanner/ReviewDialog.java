package com.example.myplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/******************************************************************************************************************************
 The purpose of this class is to display a review dialog that has the users rate their performances on tasks that have expired.
 *****************************************************************************************************************************/

public class ReviewDialog extends AppCompatDialogFragment {
    String task_string;
    TextView task;
    EditText review;
    Spinner mySpinner;

    ReviewDialog(String _task) {
        task_string = _task;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());
        setCancelable(false); // prevents the dialog fragment from closing when the user presses the back button


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View myView = inflater.inflate(R.layout.reviewtask_layout, null);

        myBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this function is left empty on purpose
            }
        });

        myBuilder.setView(myView);

        final AlertDialog myDialog = myBuilder.create();
        myDialog.show();
        myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(myDialog);
            }
        });


        task = (TextView) myView.findViewById(R.id.reviewDialogBox_taskName);
        review = (EditText) myView.findViewById(R.id.reviewDialogBox_review);
        mySpinner = (Spinner) myView.findViewById(R.id.mySpinner);


        task.setText(task_string);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.grades));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


        return myDialog;
    }

    // After the user writes their review on their performance for a certain task, this method updates
    // the tables in the databases
    private void updateDatabase(AlertDialog myDialog) {
        if (review.getText().toString().equals(""))
            Toast.makeText(getContext(), "Please review your performance on the task", Toast.LENGTH_SHORT).show();
        else {
            DatabaseHandler iGROW_db = new DatabaseHandler(getContext());
            Cursor res = iGROW_db.getTaskFromTable3(task.getText().toString());

            res.moveToNext();
            String description = res.getString(1);
            int requestCode = res.getInt(2);
            String time = res.getString(3);


            // Adds the task to "Expired_Tasks_Table"
            iGROW_db.insertIntoTable2(task.getText().toString(), description, requestCode, time, review.getText().toString(), mySpinner.getSelectedItem().toString());

            // Updates the list of expired tasks
            ExpiredTaskFragment.expiredTasks_Array.add(task.getText().toString());
            ArrayAdapter<String> myArrayAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ExpiredTaskFragment.expiredTasks_Array);
            ExpiredTaskFragment.expiredTasks_ListView.setAdapter(myArrayAdapter1);

            ExpiredTaskFragment.tasksAboutToBeReviewed.remove(task);


            // Removes the task from "Current_Tasks_Table"
            iGROW_db.deleteFromTable1(task.getText().toString());
            CurrentTaskFragment.currentTasks_Array.remove(task.getText().toString());

            // Removes the task from table 3
            iGROW_db.deleteFromTable3(task.getText().toString());


            myDialog.dismiss();

            /*
            if (CurrentTaskFragment.currentTasks_ListView != null) {
                ArrayAdapter<String> myArrayAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, CurrentTaskFragment.currentTasks_Array);
                CurrentTaskFragment.currentTasks_ListView.setAdapter(myArrayAdapter2);
            }
             */
        }
    }

}
