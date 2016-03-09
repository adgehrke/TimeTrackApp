package com.example.adriangehrke.timetrackapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adriangehrke.timetrackapp.R;
import com.example.adriangehrke.timetrackapp.Timetrack;
import com.example.adriangehrke.timetrackapp.database.DbHelper;
import com.example.adriangehrke.timetrackapp.models.Project;
import com.example.adriangehrke.timetrackapp.models.Worksession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShowProjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long time = 0;
    private long startTime = 0;
    private boolean started = false;

    private int n=0;

    Timer timer;
    TimerTask timerTask;
    TextView stopwatchStatusTxt;
    Timetrack app;

    DbHelper mDbHelper;
    SQLiteDatabase db;

    Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showproject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDatabase();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        app = (Timetrack) getApplicationContext();






    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.settings_saved);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, id+" ", duration);
        toast.show();

        TextView usernameTxt;
        TextView usermailTxt;
        usernameTxt = (TextView)findViewById(R.id.username);
        usermailTxt = (TextView)findViewById(R.id.usermail);

        ArrayList<Worksession> worksessions = mDbHelper.getWorksessionsByProjectId(db, id);
        if (worksessions.size() > 0){
            context = getApplicationContext();

            final ListView listview = (ListView) findViewById(R.id.worksessions);


            final ArrayList<String> list = new ArrayList<String>();

            for (int i = 0; i < worksessions.size(); ++i) {

                list.add(getProjectById(worksessions.get(i).getProjectId()).getName()+" "+worksessions.get(i).getDuration());
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
        else{

        }


    }

    private Project getProjectById(int id){
        ArrayList<Project> projects = mDbHelper.getProjectList(db);
        for(Project project : projects){
            if (project.getId() == id){
                return project;
            }
        }
        return null;
    }





    private void initDatabase(){
        mDbHelper = new DbHelper(this);
        db = mDbHelper.getWritableDatabase();
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
            //moveTaskToBack(true);
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
            intent = new Intent(this, ShowProjectActivity.class);
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
