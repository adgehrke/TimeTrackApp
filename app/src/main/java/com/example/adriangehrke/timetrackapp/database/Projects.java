package com.example.adriangehrke.timetrackapp.database;

import android.provider.BaseColumns;

/**
 * Created by adriangehrke on 04.03.16.
 */
public final class Projects {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Projects() {}

    /* Inner class that defines the table contents */
    public static abstract class ProjectEntry implements BaseColumns {
        public static final String TABLE_NAME = "project";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ID = "id";

    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ProjectEntry.TABLE_NAME + " (" +
                    ProjectEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    ProjectEntry.COLUMN_NAME_NAME + TEXT_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProjectEntry.TABLE_NAME;
}
