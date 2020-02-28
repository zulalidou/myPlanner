package com.example.myplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*************************************************************************************************************************
 The purpose of this fragment class is to provide the users with some information about the goal the app tries to achieve,
 and how it operates in general.
 ************************************************************************************************************************/

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.toolbar.setTitle("ABOUT");

        View myView = inflater.inflate(R.layout.fragment_about, container, false);
        return myView;
    }
}
