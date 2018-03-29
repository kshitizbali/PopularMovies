package com.udacity.kshitiz.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.udacity.kshitiz.popularmovies.MainActivityNew;
import com.udacity.kshitiz.popularmovies.data.MoviesContract;
import com.udacity.kshitiz.popularmovies.utilties.ConstantUtilities;
import com.udacity.kshitiz.popularmovies.utilties.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Kshitiz on 2/27/2018.
 */

public class PopMoviesSyncTask {

    synchronized public static void syncMovies(Context context, int loaderId) {
        try {
            URL moviesRequestUrl;
            String moviesBy;

            if (loaderId == MainActivityNew.ID_MOVIES_LOADER) {
                moviesRequestUrl = NetworkUtilities.buildLaunchUrl();
                moviesBy = ConstantUtilities.MOVIES_ON_START_UP;
            } else if (loaderId == MainActivityNew.ID_MOVIES_LOADER_POPULAR) {
                moviesRequestUrl = NetworkUtilities.buildUrlStock(ConstantUtilities.POPULAR_PARAM);
                moviesBy = ConstantUtilities.POPULAR_PARAM;
            } else if (loaderId == MainActivityNew.ID_MOVIES_LOADER_TOP_RATED) {
                moviesRequestUrl = NetworkUtilities.buildUrlStock(ConstantUtilities.TOP_RATED_PARAM);
                moviesBy = ConstantUtilities.TOP_RATED_PARAM;
            } /*else if (loaderId == MainActivityNew.ID_MOVIES_LOADER_MY_FAV) {

            }*/ else {
                throw new RuntimeException("Loader Id not valid: " + loaderId);
            }

            Log.i("URL FOR RESPONSE", moviesRequestUrl.toString());
            String jsonWeatherResponse = NetworkUtilities.getResponseFromHttpUrl(moviesRequestUrl);

            ContentValues[] moviesValues = NetworkUtilities.getMoviesContentValuesFromJson(context, jsonWeatherResponse, moviesBy);


            if (moviesValues != null && moviesValues.length != 0) {

                ContentResolver popMoviesContentResolver = context.getContentResolver();

                popMoviesContentResolver.delete(MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COLUMN_MOVIES_BY, new String[]{moviesBy});

                popMoviesContentResolver.bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, moviesValues);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
