package com.example.myplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();



        // To listen to click events on our navigation view, we need a reference to it.. hence, the variable below
        NavigationView myNavView = findViewById(R.id.nav_view);
        myNavView.setNavigationItemSelectedListener(this);


        // Rotating the device causes the app to destroy the current activity & recreate it. If the users were in another
        // activity, the activity would get destroyed and the newly created one would open the "CurrentTask" fragement,
        // instead of the fragment that the user was previously on. The code below helps avoid that.
        // -- savedInstanceState is null if the activity is started for the first time, or we leave the current activity
        // -- through the back button, and get back to it. Rotating the device will cause "savedInstanceState to not be null
        if (savedInstanceState == null) {
            // The code below makes the "CurrentTasks" fragment be the first fragment to be displayed
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CurrentTaskFragment()).commit();

            myNavView.setCheckedItem(R.id.nav_currentTasks);
        }
    }


    // The "listener" method for the navigation view. Determines which fragments to show based on
    // which item/tab pressed by the user
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_currentTasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CurrentTaskFragment()).commit();
                break;
            case R.id.nav_expiredTasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ExpiredTaskFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START); // closes the drawer
        return true; // this means an item/tab (from the navigation view) will be selected..
    }


    // When we press the back button when the navigation drawer is opened, we don't want to leave
    // the activity immediately. We instead want to close the navigation drawer.
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
