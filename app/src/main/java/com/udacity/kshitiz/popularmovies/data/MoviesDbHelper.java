package com.udacity.kshitiz.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kshitiz on 2/9/2018.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = " CREATE TABLE " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " LONG NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIES_BY + " TEXT NOT NULL, " +
                " UNIQUE (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
                /*");";*/

        final String SQL_CREATE_FAV_MOVIES_TABLE = " CREATE TABLE " +
                MoviesContract.MoviesEntry.TABLE_NAME_FAV + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " LONG NOT NULL, " +
                " UNIQUE (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_FAV_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);*/

    }
}
