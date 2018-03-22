package com.udacity.kshitiz.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.kshitiz.popularmovies.data.MoviesDbHelper;

/**
 * Created by Kshitiz on 2/15/2018.
 */

public class MoviesContentProvider extends ContentProvider {

    public static final int CODE_FAV_MOVIES = 100;
    public static final int CODE_FAV_MOVIES_WITH_ID = 101;

    public static final int CODE_MY_FAV_MOVIES = 201;
    public static final int CODE_MY_FAV_MOVIES_WITH_ID = 202;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mMoviesDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAV_MOVIES, CODE_FAV_MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAV_MOVIES + "/#", CODE_FAV_MOVIES_WITH_ID);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MY_FAV_MOVIES, CODE_MY_FAV_MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MY_FAV_MOVIES + "/#", CODE_MY_FAV_MOVIES_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {

        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAV_MOVIES:

                cursor = mMoviesDbHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_NAME, projection, MoviesContract.MoviesEntry.COLUMN_MOVIES_BY + " = ? ", selectionArgs, null, null, sortOrder);
                break;
            case CODE_MY_FAV_MOVIES:

                cursor = mMoviesDbHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_NAME_FAV, projection, null, null, null, null, null);

                break;
            case CODE_MY_FAV_MOVIES_WITH_ID:

                String id = uri.getPathSegments().get(1);
                cursor = mMoviesDbHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_NAME_FAV, projection, selection, new String[]{id}, null, null, null, "1");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;
        long id;

        switch (match) {
            case CODE_FAV_MOVIES:

                id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);

                if (id > 0) {
                    //success
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);

                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;

            case CODE_MY_FAV_MOVIES:

                id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME_FAV, null, values);

                if (id > 0) {
                    //success
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);

                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int taskDeleted;
        String id;

        switch (match) {
            case CODE_FAV_MOVIES_WITH_ID:
                id = uri.getPathSegments().get(1);
                taskDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{id});
                break;
            case CODE_FAV_MOVIES:
                taskDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_MOVIES_BY + "=?", selectionArgs);
                break;

            case CODE_MY_FAV_MOVIES:
                taskDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME_FAV, selection, selectionArgs);
                break;
            case CODE_MY_FAV_MOVIES_WITH_ID:
                id = uri.getPathSegments().get(1);
                taskDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME_FAV, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (taskDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return taskDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int taskUpdated;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_MY_FAV_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);

                final String whereClause = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
                final String[] whereArgs = {id};


                taskUpdated = mMoviesDbHelper.getWritableDatabase().update(MoviesContract.MoviesEntry.TABLE_NAME_FAV, values, whereClause, whereArgs);

                //UPDATE ORDERTABLE SET QUANTITY = (INSERT VALUE OF YOUR EDIT TEXT)WHERE NAME = 'Order2'

                //String update = " UPDATE " + MoviesContract.MoviesEntry.TABLE_NAME_FAV + " SET " + MoviesContract.MoviesEntry.COLUMN_FAV_FLAG + " = " + values + " WHERE " + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=" + id;

                // taskUpdated = mMoviesDbHelper.getWritableDatabase().execSQL(update);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }

        if (taskUpdated != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return taskUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();


        switch (sUriMatcher.match(uri)) {

            case CODE_FAV_MOVIES:

                db.beginTransaction();
                int rowsInserted = 0;

                try {

                    for (ContentValues contentValues : values) {
                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, contentValues);
                        if (_id != 1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }


    }
}
