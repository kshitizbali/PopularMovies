package com.udacity.kshitiz.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.udacity.kshitiz.popularmovies.R;

/**
 * Created by Kshitiz on 2/28/2018.
 */

public class PopMoviesFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchMoviesTask;
    int ID_LOADER;

    @Override
    public boolean onStartJob(final JobParameters job) {


        if (job.getExtras() != null) {
            ID_LOADER = job.getExtras().getInt(getResources().getString(R.string.ID_LOADER));
        }


        mFetchMoviesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                PopMoviesSyncTask.syncMovies(context, ID_LOADER);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                /*super.onPostExecute(aVoid);*/
                jobFinished(job, false);
            }
        };

        mFetchMoviesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchMoviesTask != null) {
            mFetchMoviesTask.cancel(true);
        }
        return true;
    }
}
