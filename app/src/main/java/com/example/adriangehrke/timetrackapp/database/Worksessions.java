package com.example.adriangehrke.timetrackapp.database;

import android.provider.BaseColumns;

/**
 * Created by adriangehrke on 04.03.16.
 */
public final class Worksessions {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Worksessions() {}

    /* Inner class that defines the table contents */
    public static abstract class WorksessionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "worksession";
        public static final String COLUMN_NAME_PROJECT = "project";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_ID = "id";

    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";

    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WorksessionsEntry.TABLE_NAME + " (" +
                    WorksessionsEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    WorksessionsEntry.COLUMN_NAME_PROJECT + INTEGER_TYPE + COMMA_SEP +
                    WorksessionsEntry.COLUMN_NAME_DURATION + TEXT_TYPE +

                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WorksessionsEntry.TABLE_NAME;
}
