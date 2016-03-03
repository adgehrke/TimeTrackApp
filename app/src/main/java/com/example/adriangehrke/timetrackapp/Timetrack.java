package com.example.adriangehrke.timetrackapp;

import android.app.Application;
import android.view.View;

import java.util.Calendar;

/**
 * Created by adriangehrke on 03.03.16.
 */
public class Timetrack extends Application {
    private int stopwatchSecs = 0;
    private long time = 0;
    private long startTime = 0;
    private boolean started = false;

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

                            System.out.println("Gestartet"+stopwatchSecs);
                        }

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
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
}
