package com.example.adriangehrke.timetrackapp;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class StopwatchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long time = 0;
    private long startTime = 0;
    private boolean started = false;
    DbHelper mDbHelper;
    SQLiteDatabase db;

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
                Clients.ClientEntry._ID,
                Clients.ClientEntry.COLUMN_NAME_TITLE,
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Clients.ClientEntry._ID + " DESC";

        ArrayList<String> values = new ArrayList<String>();
        int a = 0;
        try (Cursor c = db.query(
                Clients.ClientEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        )) {
            while (c.moveToNext()) {
                values.add(c.getString(c.getColumnIndex(Clients.ClientEntry.COLUMN_NAME_TITLE)));
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
    }

    /** Called when the user touches the button */
    public void stopTimer(View view) {
        app.setStarted(false);
        app.addWorksession(db, 1, 100);

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

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_gallery) {

        }
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 1);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
