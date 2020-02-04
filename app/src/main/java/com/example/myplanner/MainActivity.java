package com.example.myplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;

    private NavigationView myNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // To listen to click events on our navigation view, we need a reference to it.. hence, the variable below
        myNavView = findViewById(R.id.nav_view);
        myNavView.setNavigationItemSelectedListener(this);


        // Rotating the device causes the app to destroy the current activity & recreate it. If the users were in another
        // activity, the activity would get destroyed and the newly created one would open the "CurrentTask" fragment,
        // instead of the fragment that the user was previously on. The code below helps avoid that.
        // -- savedInstanceState is null if the activity is started for the first time, or we leave the current activity
        //    through the back button, and get back to it. Rotating the device will cause "savedInstanceState to not be null
        if (savedInstanceState == null) {
            if (getIntent().getStringExtra("A task has just expired") != null) {
                myNavView.setCheckedItem(R.id.nav_expiredTasks);

                // The code below displays the "ExpiredTasks" fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExpiredTaskFragment()).commit();
            }
            else {
                myNavView.setCheckedItem(R.id.nav_currentTasks);

                // The code below displays the "CurrentTasks" fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentTaskFragment()).commit();
            }
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

    /*
    private void openExpiredTasksFragment() {
        Toast.makeText(this, "openExpiredTasksFragment() called", Toast.LENGTH_SHORT).show();

        ExpiredTaskFragment newFragment = new ExpiredTaskFragment();
        FragmentTransaction myFragTrans = getSupportFragmentManager().beginTransaction();
        myFragTrans.replace(R.id.fragment_container, newFragment).commit();
    }
     */

    // Link: http://www.helloandroid.com/tutorials/communicating-between-running-activities
    // There's only one activity, which is MainActivity, and it's always at the top of the stack. Suppose a user is using the app and the notification
    // notifying the users that the time set for a task has expired. When the user touches/clicks the notification, instead of a new instance of the
    // MainActivity class being called, this function below gets called instead.
    // - This method gets executed (instead of the onCreate method) if we're trying to create a new instance of an activity that's already at the top
    //   of the foreground task, and that activity's launchMode is set to "singleTop"
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // replaces the old intent with the new intent created by clicking the notification

        if (getIntent().getStringExtra("A task has just expired") != null) {
            myNavView.setCheckedItem(R.id.nav_expiredTasks);

            // The code below displays the "ExpiredTasks" fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExpiredTaskFragment()).commit();
        }
        else {
            myNavView.setCheckedItem(R.id.nav_currentTasks);

            // The code below displays the "CurrentTasks" fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentTaskFragment()).commit();
        }
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
