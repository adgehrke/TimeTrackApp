package com.example.adriangehrke.timetrackapp.activities;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adriangehrke.timetrackapp.database.DbHelper;
import com.example.adriangehrke.timetrackapp.database.Projects;
import com.example.adriangehrke.timetrackapp.R;
import com.example.adriangehrke.timetrackapp.Timetrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddprojectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long time = 0;
    private long startTime = 0;
    private boolean started = false;

    Timer timer;
    TimerTask timerTask;
    TextView tv1;
    TextView tv2;
    Timetrack app;

    DbHelper mDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        app = (Timetrack) getApplicationContext();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();
    }

    public void addClient(View view){
        EditText hourlyRateValue = (EditText)findViewById(R.id.clientsAddHourlyRate);
        int hourlyRate = Integer.valueOf(hourlyRateValue.getText().toString().trim());

        EditText nameValue = (EditText)findViewById(R.id.clientsAddName);
        String name = nameValue.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(Projects.ProjectEntry.COLUMN_NAME_NAME, name);


// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                Projects.ProjectEntry.TABLE_NAME,
                "y",
                values);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }



    public void fillBtn(View view){
        db.execSQL(Projects.SQL_DELETE_ENTRIES);
        db.execSQL(Projects.SQL_CREATE_ENTRIES);
        ContentValues values = new ContentValues();
        values.put(Projects.ProjectEntry.COLUMN_NAME_NAME, "aaa");


// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(Projects.ProjectEntry.TABLE_NAME,"y",values);
    }

    public void readBtn(View view) {
        String[] projection = {
                Projects.ProjectEntry._ID,
                Projects.ProjectEntry.COLUMN_NAME_NAME,
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Projects.ProjectEntry._ID + " DESC";

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


        Context context = getApplicationContext();
        CharSequence text = values.get(values.size()-1);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        final ListView listview = (ListView) findViewById(R.id.listview);


        final ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i < values.size(); ++i) {
            list.add(values.get(i));
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        list.remove(item);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
