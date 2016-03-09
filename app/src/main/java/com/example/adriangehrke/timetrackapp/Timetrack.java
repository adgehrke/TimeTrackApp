package com.example.adriangehrke.timetrackapp;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.adriangehrke.timetrackapp.database.Projects;
import com.example.adriangehrke.timetrackapp.models.Project;
import com.example.adriangehrke.timetrackapp.database.Worksessions;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by adriangehrke on 03.03.16.
 */
public class Timetrack extends Application {
    private int stopwatchSecs = 0;
    private long time = 0;
    private long startTime = 0;
    private boolean started = false;
    private int hourlyRate = 40;




    public Timetrack(){



        System.out.println("Gestartet");
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        if (started){
                            Calendar c = Calendar.getInstance();
                            long time = System.currentTimeMillis();
                            stopwatchSecs = Math.round(time - startTime)/1000;

                            //System.out.println("Gestartet"+stopwatchSecs);
                        }

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }



    public int getHourlyRate(){
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate){
        this.hourlyRate = hourlyRate;
    }

    public boolean getEnabled(){
        return started;
    }

    public int getStopWatchSecs(){
        return stopwatchSecs;
    }

    /** Called when the user touches the button */
    public void stopTimer() {
        started = false;

    }

    public void startTimer() {
        started = true;
        Calendar c = Calendar.getInstance();
        startTime = System.currentTimeMillis();

        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        //tv2.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
    }

    public void setStarted(boolean started){
        this.started = started;
    }
    public void updateStartedTime(){
        started = true;
        Calendar c = Calendar.getInstance();
        startTime = System.currentTimeMillis();
    }

    public void createTables(SQLiteDatabase db){
        db.execSQL(Projects.SQL_CREATE_ENTRIES);
        db.execSQL(Worksessions.SQL_CREATE_ENTRIES);
    }



    public ArrayList<Project> getClients(SQLiteDatabase db){
        String[] projection = {
                Projects.ProjectEntry._ID,
                Projects.ProjectEntry.COLUMN_NAME_NAME,
        };

        String sortOrder = Projects.ProjectEntry._ID + " DESC";

        ArrayList<Project> projects = new ArrayList<Project>();
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
                projects.add(new Project(c.getPosition(),c.getString(c.getColumnIndex(Projects.ProjectEntry.COLUMN_NAME_NAME))));
            }
        }


       return projects;

    }



}
