package com.example.myplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*************************************************************************************************************************
 The purpose of this fragment class is to credit those whose images were used in this app.
 ************************************************************************************************************************/

public class CreditsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.toolbar.setTitle("CREDITS");

        View myView = inflater.inflate(R.layout.fragment_credits, container, false);
        return myView;
    }
}
