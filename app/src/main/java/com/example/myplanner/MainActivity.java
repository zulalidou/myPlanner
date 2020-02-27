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

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView myNavView;

    private AlarmManager alarmMgr;
    private PendingIntent myPendingIntent;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDailyOperation();


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


















        // Rotating the device causes the app to destroy the current activity & recreate it. If the users were in another
        // activity, the activity would get destroyed and the newly created one would open the "CurrentTask" fragment,
        // instead of the fragment that the user was previously on. The code below helps avoid that.
        // -- savedInstanceState is null if the activity is started for the first time, or we leave the current activity
        //    through the back button, and get back to it. Rotating the device will cause "savedInstanceState to not be null
        if (savedInstanceState == null) {
            DatabaseHandler iGROW_db = new DatabaseHandler(getBaseContext());
            Cursor res = iGROW_db.getItemsFromTable3();

            // -----------------------------------------------------------------------------------------------------------------------------------------------------
            // 1st case: When a task expires, it gets placed in the ReviewDialog table. When the users head to the "expiredTaskFragment", but fail to review their
            // performance & exit the app, we need to make sure that the next time they open the open, they're immediately prompted to review the expired task(s).
            // So if there exists at least 1 task in the ReviewDialog table, the "if" condition below will be executed
            // -----------------------------------------------------------------------------------------------------------------------------------------------------
            // 2nd case: When the users click the notification for a task that just expired (and there is currently no task history of the app on the users' phones)
            // -----------------------------------------------------------------------------------------------------------------------------------------------------
            if (res.getCount() >= 1 || getIntent().getStringExtra("A task has just expired") != null) {
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

    private void setDailyOperation() {

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ClearDatabase.class);
        myPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


        myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(System.currentTimeMillis());
        myCalendar.set(Calendar.HOUR_OF_DAY, 23);
        myCalendar.set(Calendar.MINUTE, 59);
        myCalendar.set(Calendar.SECOND, 0);

        // setRepeating() lets you specify a precise custom interval--in this case 2 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, myPendingIntent);
    }


    private void setToolbarTitle() {
        if (getIntent().getStringExtra("A task has just expired") != null)
            toolbar.setTitle("Expired tasks");
        else
            toolbar.setTitle("Current tasks");
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

        if (getIntent().getStringExtra("A task has just expired") != null) {
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
