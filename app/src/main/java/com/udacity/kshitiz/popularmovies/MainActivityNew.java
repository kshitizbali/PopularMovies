package com.udacity.kshitiz.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.kshitiz.popularmovies.adapter.MoviesListAdapterNew;
import com.udacity.kshitiz.popularmovies.data.MoviesContract;
import com.udacity.kshitiz.popularmovies.sync.PopMoviesSyncUtilities;
import com.udacity.kshitiz.popularmovies.utilties.ConstantUtilities;
import com.udacity.kshitiz.popularmovies.utilties.NetworkUtilities;

/**
 * Created by Kshitiz on 3/1/2018.
 */

public class MainActivityNew extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MoviesListAdapterNew.MovieListAdapterOnClickHandler {

    private TextView tvError;
    private Toast mToast;

    public static final String TAG = MainActivityNew.class.getSimpleName();

    public static final String[] MOVIES_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIES_BY};

    public static final String[] FAV_MOVIES_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID};

    public static final int INDEX_MOVIE_NAME = 0;
    public static final int INDEX_MOVIE_POSTER = 1;
    public static final int INDEX_MOVIE_RATING = 2;
    public static final int INDEX_MOVIE_RELEASE_DATE = 3;
    public static final int INDEX_MOVIE_SYNOPSIS = 4;
    public static final int INDEX_MOVIE_ID = 5;

    public static final int ID_MOVIES_LOADER = 15;
    public static final int ID_MOVIES_LOADER_POPULAR = 16;
    public static final int ID_MOVIES_LOADER_TOP_RATED = 17;
    public static final int ID_MOVIES_LOADER_MY_FAV = 18;


    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;

    private MoviesListAdapterNew mMovieListAdapterNew;
    private int MOVIES_ID_TO_LOAD = ID_MOVIES_LOADER;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkUtilities.initializeStetho(getApplicationContext());

        populatingViews();

        if (NetworkUtilities.isInternetAvailable(MainActivityNew.this)) {
            onAppLaunch(savedInstanceState);
        } else {
            showErrorMessage(getResources().getString(R.string.check_internet_connection));
        }

        /*getSupportLoaderManager().initLoader(MOVIES_ID_TO_LOAD, null, this);
        Log.i("MOVIES TO LOAD ", "" + MOVIES_ID_TO_LOAD);

        PopMoviesSyncUtilities.initialize(this, MOVIES_ID_TO_LOAD, ConstantUtilities.MOVIES_ON_START_UP);*/
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ID_MOVIES_LOADER:

                Uri movieQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;

                return new CursorLoader(this,
                        movieQueryUri,
                        MOVIES_PROJECTION,
                        MoviesContract.MoviesEntry.COLUMN_MOVIES_BY + " ? = ",
                        new String[]{ConstantUtilities.MOVIES_ON_START_UP},
                        null);

            case ID_MOVIES_LOADER_POPULAR:

                Uri movieQueryPopUri = MoviesContract.MoviesEntry.CONTENT_URI;

                return new CursorLoader(this,
                        movieQueryPopUri,
                        MOVIES_PROJECTION,
                        MoviesContract.MoviesEntry.COLUMN_MOVIES_BY + " ? = ",
                        new String[]{ConstantUtilities.POPULAR_PARAM},
                        null);

            case ID_MOVIES_LOADER_TOP_RATED:

                Uri movieQueryTopUri = MoviesContract.MoviesEntry.CONTENT_URI;

                return new CursorLoader(this,
                        movieQueryTopUri,
                        MOVIES_PROJECTION,
                        MoviesContract.MoviesEntry.COLUMN_MOVIES_BY + " ? = ",
                        new String[]{ConstantUtilities.TOP_RATED_PARAM},
                        null);

            case ID_MOVIES_LOADER_MY_FAV:

                //Load Fav Movies

                Uri favMoviesQuery = MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV;

                return new CursorLoader(this,
                        favMoviesQuery,
                        FAV_MOVIES_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        mMovieListAdapterNew.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data != null && data.getCount() != 0) showMoviesDataView();
        else showErrorMessage("No Data.");
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

        mMovieListAdapterNew.swapCursor(null);
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mMovieListAdapterNew != null && mMovieListAdapterNew.getItemCount() != 0) {
            Log.i("ID SAVED", "" + MOVIES_ID_TO_LOAD);
            outState.putInt(getString(R.string.movies_id_to_load), MOVIES_ID_TO_LOAD);
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMovieListAdapterNew != null && mMovieListAdapterNew.getItemCount() != 0) {
            Log.i("ID SAVED", "" + MOVIES_ID_TO_LOAD);
            outState.putInt(getString(R.string.movies_id_to_load), MOVIES_ID_TO_LOAD);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null || savedInstanceState.containsKey(getString(R.string.movies_id_to_load))) {

            showLoading();
            int savedID = savedInstanceState.getInt(getString(R.string.movies_id_to_load));

            String movieFlag = "";
            if (savedID == ID_MOVIES_LOADER) {
                movieFlag = ConstantUtilities.MOVIES_ON_START_UP;

                getSupportLoaderManager().initLoader(savedID, null, this);
                PopMoviesSyncUtilities.initialize(this, savedID, movieFlag);
                MOVIES_ID_TO_LOAD = savedID;

            } else if (savedID == ID_MOVIES_LOADER_POPULAR) {
                movieFlag = ConstantUtilities.POPULAR_PARAM;

                getSupportLoaderManager().initLoader(savedID, null, this);
                PopMoviesSyncUtilities.initialize(this, savedID, movieFlag);

                MOVIES_ID_TO_LOAD = savedID;
            } else if (savedID == ID_MOVIES_LOADER_TOP_RATED) {
                movieFlag = ConstantUtilities.TOP_RATED_PARAM;

                getSupportLoaderManager().initLoader(savedID, null, this);
                PopMoviesSyncUtilities.initialize(this, savedID, movieFlag);

                MOVIES_ID_TO_LOAD = savedID;

            } else if (savedID == ID_MOVIES_LOADER_MY_FAV) {
                getSupportLoaderManager().initLoader(savedID, null, this);
                MOVIES_ID_TO_LOAD = savedID;
            }

        } else {
            getSupportLoaderManager().initLoader(MOVIES_ID_TO_LOAD, null, this);
            PopMoviesSyncUtilities.initialize(this, MOVIES_ID_TO_LOAD, ConstantUtilities.MOVIES_ON_START_UP);
        }
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onClick(int movieListItemPosition, Cursor cursor) {

        //Intent to next activity

        cursor.moveToPosition(movieListItemPosition);

        String mTitle = "", mSynopsis = "", mUserRating = "", mReleaseDate = ConstantUtilities.getCurrentDate(), movieId = "", mPoster = "";
        int favFlag;

        //Check for null
        if (cursor.getString(INDEX_MOVIE_NAME) != null) {
            mTitle = cursor.getString(INDEX_MOVIE_NAME);
        }

        if (cursor.getString(INDEX_MOVIE_SYNOPSIS) != null) {
            mSynopsis = cursor.getString(INDEX_MOVIE_SYNOPSIS);
        }
        if (cursor.getString(INDEX_MOVIE_RATING) != null) {
            mUserRating = cursor.getString(INDEX_MOVIE_RATING);
        }
        if (cursor.getString(INDEX_MOVIE_RELEASE_DATE) != null) {
            mReleaseDate = cursor.getString(INDEX_MOVIE_RELEASE_DATE);
        }
        if (cursor.getString(INDEX_MOVIE_ID) != null) {
            movieId = cursor.getString(INDEX_MOVIE_ID);
        }

        if (cursor.getString(INDEX_MOVIE_POSTER) != null) {
            mPoster = cursor.getString(INDEX_MOVIE_POSTER);
        }


        Intent intentToStartDetailActivity = new Intent(MainActivityNew.this, MovieDetailActivityNew.class);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_POSTER, mPoster);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE, mTitle);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_SYNOPSIS, mSynopsis);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING, mUserRating);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE, mReleaseDate);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_ID, movieId);
        /*if (cursor.getColumnName(7).equals(MoviesContract.MoviesEntry.COLUMN_FAV_FLAG)) {
            favFlag = cursor.getInt(INDEX_MOVIE_FAV_FLAG);
            intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_FAV_FLAG, favFlag);
        }*/

        startActivity(intentToStartDetailActivity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sort:


                break;
            case R.id.action_dropdown_popular:


                MOVIES_ID_TO_LOAD = ID_MOVIES_LOADER_POPULAR;
                getSupportLoaderManager().restartLoader(MOVIES_ID_TO_LOAD, null, this);

                PopMoviesSyncUtilities.initialize(this, MOVIES_ID_TO_LOAD, ConstantUtilities.POPULAR_PARAM);
                /*loadMoviesBySortOrder(ConstantUtilities.SORT_BY_POPULAR, ConstantUtilities.VOTE_COUNT_RESET, getString(R.string.popular_movies));*/

                //loadMoviesBySortOrderStock(ConstantUtilities.POPULAR_PARAM, getString(R.string.popular_movies));

                break;

            case R.id.action_dropdown_top_rated:

                MOVIES_ID_TO_LOAD = ID_MOVIES_LOADER_TOP_RATED;

                getSupportLoaderManager().restartLoader(MOVIES_ID_TO_LOAD, null, this);

                PopMoviesSyncUtilities.initialize(this, MOVIES_ID_TO_LOAD, ConstantUtilities.TOP_RATED_PARAM);
                /*loadMoviesBySortOrder(ConstantUtilities.SORT_BY_TOP_RATED, ConstantUtilities.VOTE_COUNT_5k, getString(R.string.top_rated_movies));*/


                //loadMoviesBySortOrderStock(ConstantUtilities.TOP_RATED_PARAM, getString(R.string.top_rated_movies));

                break;

            case R.id.action_dropdown_my_fav:

                MOVIES_ID_TO_LOAD = ID_MOVIES_LOADER_MY_FAV;
                getSupportLoaderManager().restartLoader(MOVIES_ID_TO_LOAD, null, this);

                break;

            /*default:
                MOVIES_ID_TO_LOAD = ID_MOVIES_LOADER;
                break;*/

        }

        return super.onOptionsItemSelected(item);
    }

    //Populating Layout Views
    private void populatingViews() {

        tvError = (TextView) findViewById(R.id.tv_error_msg);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapterNew = new MoviesListAdapterNew(this, this);

        mRecyclerView.setAdapter(mMovieListAdapterNew);


    }


    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        tvError.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showMoviesDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);

        tvError.setVisibility(View.INVISIBLE);
    }

    //shows error messages
    private void showErrorMessage(String errorText) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        tvError.setText(errorText);
        tvError.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void showToastMsg(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);

        mToast.show();
    }


    private void onAppLaunch(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movies_id_to_load))) {


            showLoading();
            getSupportLoaderManager().initLoader(MOVIES_ID_TO_LOAD, null, this);
            Log.i("MOVIES TO LOAD ", "" + MOVIES_ID_TO_LOAD);
            PopMoviesSyncUtilities.initialize(this, MOVIES_ID_TO_LOAD, ConstantUtilities.MOVIES_ON_START_UP);

        } else {
            showLoading();
            int savedID = savedInstanceState.getInt(getString(R.string.movies_id_to_load));

            String movieFlag = "";
            if (savedID == ID_MOVIES_LOADER) {
                movieFlag = ConstantUtilities.MOVIES_ON_START_UP;
                getSupportLoaderManager().initLoader(savedID, null, this);
                PopMoviesSyncUtilities.initialize(this, savedID, movieFlag);
            } else if (savedID == ID_MOVIES_LOADER_POPULAR) {
                movieFlag = ConstantUtilities.POPULAR_PARAM;
                getSupportLoaderManager().initLoader(savedID, null, this);
                PopMoviesSyncUtilities.initialize(this, savedID, movieFlag);
            } else if (savedID == ID_MOVIES_LOADER_TOP_RATED) {
                movieFlag = ConstantUtilities.TOP_RATED_PARAM;
                getSupportLoaderManager().initLoader(savedID, null, this);
                PopMoviesSyncUtilities.initialize(this, savedID, movieFlag);
            } else if (savedID == ID_MOVIES_LOADER_MY_FAV) {
                getSupportLoaderManager().initLoader(savedID, null, this);
            }
        }

    }

}
