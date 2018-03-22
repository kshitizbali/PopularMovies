package com.udacity.kshitiz.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.udacity.kshitiz.popularmovies.R;

/**
 * Created by Kshitiz on 2/27/2018.
 */

public class PopMoviesSyncIntentService extends IntentService {

    public PopMoviesSyncIntentService() {
        super("PopMoviesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getExtras().getInt(getResources().getString(R.string.ID_LOADER)) != 0) {

            PopMoviesSyncTask.syncMovies(this, intent.getExtras().getInt(getResources().getString(R.string.ID_LOADER)));
        }


    }
}
