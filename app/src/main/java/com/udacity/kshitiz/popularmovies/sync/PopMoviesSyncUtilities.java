package com.udacity.kshitiz.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.udacity.kshitiz.popularmovies.R;
import com.udacity.kshitiz.popularmovies.data.MoviesContract;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kshitiz on 2/27/2018.
 */

public class PopMoviesSyncUtilities {

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized = false;
    private static int ID_LOADER;

    private static final String POP_MOVIES_SYNC_TAG = "pop-movies-sync";


    static void scheduleFirebaseJobDispatcherSync(final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Bundle bundle = new Bundle();
        bundle.putInt(context.getResources().getString(R.string.ID_LOADER), ID_LOADER);


        Job syncPopMoviesJob = dispatcher.newJobBuilder()
                .setService(PopMoviesFirebaseJobService.class)
                .setTag(POP_MOVIES_SYNC_TAG)
                .setExtras(bundle)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_HOURS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncPopMoviesJob);

    }

    synchronized public static void initialize(final Context context, final int loaderId, final String moviesBy) {

        if (sInitialized) return;

        sInitialized = true;

        ID_LOADER = loaderId;
        scheduleFirebaseJobDispatcherSync(context);


        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                Uri moviesQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;

                String[] projectionColumns = {MoviesContract.MoviesEntry.COLUMN_MOVIES_BY};
                /*String[] projectionColumns = {MoviesContract.MoviesEntry._ID, MoviesContract.MoviesEntry.COLUMN_MOVIES_BY};*/

                Cursor cursor = context.getContentResolver().query(moviesQueryUri, projectionColumns, MoviesContract.MoviesEntry.COLUMN_MOVIES_BY, new String[]{moviesBy}, null);
                /*Cursor cursor = context.getContentResolver().query(moviesQueryUri, projectionColumns, MoviesContract.MoviesEntry.COLUMN_MOVIES_BY, new String[]{moviesBy}, null);*/

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context, loaderId);
                }

                if (cursor != null) {

                    cursor.close();
                }

            }
        });

        checkForEmpty.start();

        /*new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                Uri moviesQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;

                String[] projectionColumns = {MoviesContract.MoviesEntry._ID};

                Cursor cursor = context.getContentResolver().query(moviesQueryUri, projectionColumns, null, null, null);

                if (null == cursor && cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
                return null;
            }
        }.execute();*/
    }

    public static void startImmediateSync(final Context context, int loaderId) {

        Intent intentToSyncImmediately = new Intent(context, PopMoviesSyncIntentService.class);
        intentToSyncImmediately.putExtra(context.getString(R.string.ID_LOADER), loaderId);
        context.startService(intentToSyncImmediately);
    }
}
