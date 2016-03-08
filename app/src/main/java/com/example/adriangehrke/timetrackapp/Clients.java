package com.example.adriangehrke.timetrackapp;

import android.provider.BaseColumns;

/**
 * Created by adriangehrke on 04.03.16.
 */
public final class Clients {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Clients() {}

    /* Inner class that defines the table contents */
    public static abstract class ClientEntry implements BaseColumns {
        public static final String TABLE_NAME = "client";
        public static final String COLUMN_NAME_TITLE = "name";
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ClientEntry.TABLE_NAME + " (" +
                    ClientEntry._ID + " INTEGER PRIMARY KEY," +
                    ClientEntry.COLUMN_NAME_TITLE + TEXT_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ClientEntry.TABLE_NAME;
}
