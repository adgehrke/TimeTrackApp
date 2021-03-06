package com.example.adriangehrke.timetrackapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adriangehrke.timetrackapp.database.DbHelper;
import com.example.adriangehrke.timetrackapp.database.Projects;
import com.example.adriangehrke.timetrackapp.R;
import com.example.adriangehrke.timetrackapp.Timetrack;
import com.example.adriangehrke.timetrackapp.models.Project;
import com.example.adriangehrke.timetrackapp.models.Worksession;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StopwatchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long time = 0;
    private long startTime = 0;
    private boolean started = false;
    DbHelper mDbHelper;
    SQLiteDatabase db;
    public int position=1;
    Spinner mySpinner;

    Timer timer;
    TimerTask timerTask;
    TextView tv1;
    TextView tv2;
    TextView earnedMoney;
    Timetrack app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();

        String[] projection = {
                Projects.ProjectEntry.COLUMN_NAME_ID,
                Projects.ProjectEntry.COLUMN_NAME_NAME,
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Projects.ProjectEntry.COLUMN_NAME_ID + " DESC";

        ArrayList<String> values = new ArrayList<String>();
        int a = 0;
        try (Cursor c = db.query(
                Projects.ProjectEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        )) {
            while (c.moveToNext()) {
                values.add(c.getString(c.getColumnIndex(Projects.ProjectEntry.COLUMN_NAME_NAME)));
                a++;
            }
        }

// Create an ArrayAdapter using the string array and a default spinner layout
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        app = (Timetrack) getApplicationContext();
        this.tv1 = (TextView)findViewById(R.id.timeTxt);
        this.tv2 = (TextView)findViewById(R.id.startTime);
        this.earnedMoney = (TextView)findViewById(R.id.earnedMoneyTxt);
        tv1.setText("00");
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                app = (Timetrack) getApplicationContext();
                                int stopWatchVal = app.getStopWatchSecs();
                                int stopWatchSecs = 0;
                                int stopWatchMinutes = 0;
                                int stopWatchHours = 0;

                                if (stopWatchVal > 3600){
                                    stopWatchHours = stopWatchVal/3600;
                                    stopWatchMinutes = (stopWatchVal-3600*stopWatchHours)/60;
                                    stopWatchSecs = stopWatchVal%60;
                                }
                                else if (stopWatchVal > 60){
                                    stopWatchMinutes = stopWatchVal/60;
                                    stopWatchSecs = stopWatchVal%60;
                                }
                                else{
                                    stopWatchSecs = stopWatchVal%60;
                                }
                                tv1.setText(String.format("%02d", stopWatchHours)+":"+String.format("%02d", stopWatchMinutes)+":"+String.format("%02d", stopWatchSecs));

                                double earnedMoneyVal = (Math.round(stopWatchVal / 60.0 / 60.0 * app.getHourlyRate()*100.0) / 100.0);
                                earnedMoney.setText(String.format("%1$,.2f", earnedMoneyVal)+" €");
                            }
                        });
                            Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        mySpinner=(Spinner) findViewById(R.id.spinner);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                ArrayList<Project> projects = mDbHelper.getProjectList(db);
                position = projects.get(pos).getId();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /** Called when the user touches the button */
    public void stopTimer(View view) {
        app.setStarted(false);
        mDbHelper.addWorksession(db, position, app.getStopWatchSecs());

    }

    public void startTimer(View view) {
        app.setStarted(true);
        app.updateStartedTime();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, StopwatchActivity.class);

        if (id == R.id.nav_dashboard) {
            intent = new Intent(this, DashboardActivity.class);
        } else if (id == R.id.nav_stopwatch) {
            intent = new Intent(this, StopwatchActivity.class);
        }
        else if (id == R.id.nav_projects) {
            intent = new Intent(this, ProjectActivity.class);
        }
        else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 1);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
