package com.example.adriangehrke.timetrackapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adriangehrke.timetrackapp.models.Project;
import com.example.adriangehrke.timetrackapp.models.Worksession;

import java.util.ArrayList;

/**
 * Created by adriangehrke on 04.03.16.
 */public class DbHelper extends SQLiteOpenHelper {



    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Projects.SQL_DELETE_ENTRIES;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TimeTrack.db";

    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Projects.SQL_CREATE_ENTRIES);
        db.execSQL(Worksessions.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Project> getProjectList(SQLiteDatabase db){
        ArrayList<Project> projects = new ArrayList<Project>();


        String[] projection = {
                Projects.ProjectEntry.COLUMN_NAME_ID,
                Projects.ProjectEntry.COLUMN_NAME_NAME,
        };

        String sortOrder = Projects.ProjectEntry.COLUMN_NAME_ID + " DESC";

        ArrayList<String> values = new ArrayList<String>();
        try (Cursor c = db.query(Projects.ProjectEntry.TABLE_NAME,projection,null, null, null, null, sortOrder)) {
            while (c.moveToNext()) {
                Project tmpProject = new Project();
                tmpProject.setId(c.getInt(c.getColumnIndex(Projects.ProjectEntry.COLUMN_NAME_ID)));
                tmpProject.setName(c.getString(c.getColumnIndex(Projects.ProjectEntry.COLUMN_NAME_NAME)));
                projects.add(tmpProject);
            }
        }


        return projects;
    }

    public ArrayList<Worksession> getWorksessionList(SQLiteDatabase db){


        ArrayList<Worksession> worksessions = new ArrayList<Worksession>();


        String[] projection = {
                Worksessions.WorksessionsEntry.COLUMN_NAME_DURATION,
                Worksessions.WorksessionsEntry.COLUMN_NAME_PROJECT,
                Worksessions.WorksessionsEntry.COLUMN_NAME_ID,


        };

        String sortOrder = Worksessions.WorksessionsEntry.COLUMN_NAME_ID + " DESC";

        ArrayList<String> values = new ArrayList<String>();
        try (Cursor c = db.query(Worksessions.WorksessionsEntry.TABLE_NAME,projection,null, null, null, null, sortOrder)) {
            while (c.moveToNext()) {
                Worksession tmpWorksession = new Worksession();
                tmpWorksession.setId(c.getInt(c.getColumnIndex(Worksessions.WorksessionsEntry.COLUMN_NAME_ID)));
                tmpWorksession.setProjectId(c.getInt(c.getColumnIndex(Worksessions.WorksessionsEntry.COLUMN_NAME_PROJECT)));
                tmpWorksession.setDuration(c.getInt(c.getColumnIndex(Worksessions.WorksessionsEntry.COLUMN_NAME_DURATION)));

                worksessions.add(tmpWorksession);
            }
        }


        return worksessions;
    }

    public void addWorksession(SQLiteDatabase db, int projectId, int duration){
        ContentValues values = new ContentValues();
        values.put(Worksessions.WorksessionsEntry.COLUMN_NAME_PROJECT, projectId);
        values.put(Worksessions.WorksessionsEntry.COLUMN_NAME_DURATION, duration);

        long newRowId;
        newRowId = db.insert(Worksessions.WorksessionsEntry.TABLE_NAME,"y",values);
    }
}