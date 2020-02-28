package com.example.myplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

/****************************************************************************************************************************
 This is the main class of the app, and is the first activity that gets executed when the app gets launched.
 ***************************************************************************************************************************/

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView myNavView;

    private AlarmManager alarmMgr;
    private PendingIntent myPendingIntent;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /******************** SETUP ********************/

        toolbar = findViewById(R.id.toolbar);
        setToolbarTitle();
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // To listen to click events on our navigation view, we need a reference to it.. hence, the variable below
        myNavView = findViewById(R.id.nav_view);
        myNavView.setNavigationItemSelectedListener(this);

        /******************** SETUP ********************/


        setDailyOperation();


        // Rotating the device causes the app to destroy the current activity & recreate it. If the users were in another
        // activity, the activity would get destroyed and the newly created one would open the "CurrentTask" fragment,
        // instead of the fragment that the user was previously on. The code below helps avoid that.
        // -- savedInstanceState is null if the activity is started for the first time, or we leave the current activity
        //    through the back button, and get back to it. Rotating the device will cause "savedInstanceState to not be null
        if (savedInstanceState == null) {
            DatabaseHandler myPlanner_DB = new DatabaseHandler(getBaseContext());
            Cursor res = myPlanner_DB.getItemsFromTable3();

            // -----------------------------------------------------------------------------------------------------------------------------------------------------
            // 1st case: When a task expires, it gets placed in the ReviewDialog table. When the users head to the "expiredTaskFragment", but fail to review their
            // performance & exit the app, we need to make sure that the next time they open the open, they're immediately prompted to review the expired task(s).
            // So if there exists at least 1 task in the ReviewDialog table, the "if" condition below will be executed
            // -----------------------------------------------------------------------------------------------------------------------------------------------------
            // 2nd case: When the users click the notification for a task that just expired (and there is currently no task history of the app on the users' phones)
            // -----------------------------------------------------------------------------------------------------------------------------------------------------
            if (res.getCount() >= 1) {
                myNavView.setCheckedItem(R.id.nav_expiredTasks);

                // The code below displays the "ExpiredTasks" fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExpiredTaskFragment()).commit();
            }

            // When there aren't any task that have expired (or HAVE expired but have also been reviewed)
            else {
                myNavView.setCheckedItem(R.id.nav_currentTasks);

                // The code below displays the "CurrentTasks" fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentTaskFragment()).commit();
            }
        }
    }


    // This method sets the toolbar based on whether or not a task has expired
    private void setToolbarTitle() {
        DatabaseHandler myPlanner_DB = new DatabaseHandler(getBaseContext());
        Cursor res = myPlanner_DB.getItemsFromTable3();

        if (res.getCount() >= 1)
            toolbar.setTitle("Expired tasks");
        else
            toolbar.setTitle("Current tasks");
    }

    // This method sets up an alarm to clear the database every day at 11:59:59pm
    private void setDailyOperation() {
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ClearDatabase.class);
        myPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(System.currentTimeMillis());
        myCalendar.set(Calendar.HOUR_OF_DAY, 23);
        myCalendar.set(Calendar.MINUTE, 59);
        myCalendar.set(Calendar.SECOND, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, myPendingIntent);
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
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
            case R.id.nav_credits:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CreditsFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START); // closes the drawer
        return true; // this means an item/tab (from the navigation view) will be selected..
    }


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

        DatabaseHandler myPlanner_DB = new DatabaseHandler(getBaseContext());
        Cursor res = myPlanner_DB.getItemsFromTable3();


        if (res.getCount() > 0) {
            myNavView.setCheckedItem(R.id.nav_expiredTasks);

            // The code below displays the "ExpiredTasks" fragment
            toolbar.setTitle("Expired tasks");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExpiredTaskFragment()).commit();
        }
        else {
            myNavView.setCheckedItem(R.id.nav_currentTasks);

            // The code below displays the "CurrentTasks" fragment
            toolbar.setTitle("Current tasks");
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
