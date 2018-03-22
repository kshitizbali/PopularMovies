package com.udacity.kshitiz.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.udacity.kshitiz.popularmovies.adapter.MovieListAdapter;
import com.udacity.kshitiz.popularmovies.utilties.ConstantUtilities;
import com.udacity.kshitiz.popularmovies.utilties.NetworkUtilities;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {

    private TextView tvError;
    private RecyclerView rvMovies;
    private ProgressBar mLoadingIndicator;
    private MovieListAdapter movieListAdapter;
    private Toast mToast;
    /*private String movieResponse = "";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populating Layout Views
        populatingViews();

        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_list))) {
            //Calling Api on First Launch (and if there is nothing saved in the bundle)
            loadMoviesDataOnFirstLaunch();
        } else {
            //bundle saved data
            showMoviesData();
            movieListAdapter.setMoviesResponse(savedInstanceState.getString(getString(R.string.movie_list)));
            movieListAdapter.setMoviesPosters(savedInstanceState.getStringArray(getString(R.string.movie_posters)));
            /*rvMovies.setAdapter(movieListAdapter);*/
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (movieListAdapter != null && movieListAdapter.getItemCount() != 0) {
            outState.putString(getString(R.string.movie_list), movieListAdapter.getMoviesResponse());
            outState.putStringArray(getString(R.string.movie_posters), movieListAdapter.getMoviePosters());
        /*outState.putString(getString(R.string.movie_list), movieResponse);*/
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.movie_list))) {
            //Calling Api on First Launch (and if there is nothing saved in the bundle)
            loadMoviesDataOnFirstLaunch();
        } else {
            //bundle saved data
            showMoviesData();
            movieListAdapter.setMoviesResponse(savedInstanceState.getString(getString(R.string.movie_list)));
            movieListAdapter.setMoviesPosters(savedInstanceState.getStringArray(getString(R.string.movie_posters)));
            rvMovies.setAdapter(movieListAdapter);
        }
        super.onRestoreInstanceState(savedInstanceState);
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


                /*loadMoviesBySortOrder(ConstantUtilities.SORT_BY_POPULAR, ConstantUtilities.VOTE_COUNT_RESET, getString(R.string.popular_movies));*/
                loadMoviesBySortOrderStock(ConstantUtilities.POPULAR_PARAM, getString(R.string.popular_movies));

                break;

            case R.id.action_dropdown_top_rated:

                /*loadMoviesBySortOrder(ConstantUtilities.SORT_BY_TOP_RATED, ConstantUtilities.VOTE_COUNT_5k, getString(R.string.top_rated_movies));*/
                loadMoviesBySortOrderStock(ConstantUtilities.TOP_RATED_PARAM, getString(R.string.top_rated_movies));

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    //Populating Layout Views
    private void populatingViews() {

        tvError = (TextView) findViewById(R.id.tv_error_msg);
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        rvMovies.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        rvMovies.setHasFixedSize(true);

        movieListAdapter = new MovieListAdapter(this);
        rvMovies.setAdapter(movieListAdapter);


    }

    @Override
    public void onClick(int movieListItemPos, String moviePoster, String movieResponse) {

        //Intent to next activity

        String mTitle = "", mSynopsis = "", mUserRating = "", mReleaseDate = ConstantUtilities.getCurrentDate(), movieId = "";


        //Check for null
        if (NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.ORIGINAL_TITLE) != null) {
            mTitle = NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.ORIGINAL_TITLE);
        }

        if (NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.PLOT_SYNOPSIS) != null) {
            mSynopsis = NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.PLOT_SYNOPSIS);
        }
        if (NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.USER_RATING) != null) {
            mUserRating = NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.USER_RATING);
        }
        if (NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.RELEASE_DATE) != null) {
            mReleaseDate = NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.RELEASE_DATE);
        }
        if (NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.ID) != null) {
            movieId = NetworkUtilities.getJsonValue(movieResponse, movieListItemPos, ConstantUtilities.ID);
        }


        Intent intentToStartDetailActivity = new Intent(MainActivity.this, MovieDetails.class);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_POSTER, moviePoster);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE, mTitle);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_SYNOPSIS, mSynopsis);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING, mUserRating);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE, mReleaseDate);
        intentToStartDetailActivity.putExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_ID, movieId);

        startActivity(intentToStartDetailActivity);


    }


    //on app first start. Loads latest releases.
    private void loadMoviesDataOnFirstLaunch() {

        new FetchMoviesOnStartUpTask().execute();

    }

    //sorting movies. By popularity & rating.
   /* private void loadMoviesBySortOrder(String sortOrder, String voteCount, String message) {

        new FetchMoviesBySortOrderTask().execute(sortOrder, voteCount, message);

    }*/


    //sorting movies. By popularity or rating.
    private void loadMoviesBySortOrderStock(String sortOrder, String message) {

        new FetchMoviesBySortOrderTaskStock().execute(sortOrder, message);

    }


    //shows movies list
    private void showMoviesData() {
        rvMovies.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.INVISIBLE);
    }

    //shows error messages
    private void showErrorMessage() {
        rvMovies.setVisibility(View.INVISIBLE);
        tvError.setVisibility(View.VISIBLE);
    }


    //remove any previous data. In case of no internet connection when we access other api's (it shows error message) but on rotation it shows the previous saved list.
    private void removePreviousData() {
        movieListAdapter.resetMoviesList();
    }

    //Sort Movies Async Task
    /*public class FetchMoviesBySortOrderTask extends AsyncTask<String, Void, String> {

        private String toastMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (NetworkUtilities.isOnlineB()) {
                String sortBy = strings[0];
                String voteCount = strings[1];
                toastMessage = strings[2];


                URL moviesRequest = NetworkUtilities.buildUrl(sortBy, voteCount);

                try {
                    *//*String jsonMoviesResponse = NetworkUtilities.getResponseFromHttpUrl(moviesRequest);*//*

                    return NetworkUtilities.getResponseFromHttpUrl(moviesRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            } else {
                if (movieListAdapter != null) {
                    removePreviousData();
                }
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.isEmpty()) {
                showMoviesData();
                *//*movieResponse = s;*//*
                movieListAdapter.setMoviesResponse(s);
                movieListAdapter.setMoviesPosters(NetworkUtilities.getMoviePoster(s));
                showToastMsg(toastMessage);
            } else {
                showErrorMessage();
            }
        }

    }*/


    //Sort Movies Async Task
    public class FetchMoviesBySortOrderTaskStock extends AsyncTask<String, Void, String> {

        private String toastMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (NetworkUtilities.isOnlineB()) {

                Log.i("Network", "Online");
                String sortBy = strings[0];
                toastMessage = strings[1];


                URL moviesRequest = NetworkUtilities.buildUrlStock(sortBy);

                try {
                    /*String jsonMoviesResponse = NetworkUtilities.getResponseFromHttpUrl(moviesRequest);*/

                    return NetworkUtilities.getResponseFromHttpUrl(moviesRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            } else {
                Log.i("Network", "Offline");
                if (movieListAdapter != null) {
                    removePreviousData();
                }
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.isEmpty()) {
                showMoviesData();
                /*movieResponse = s;*/
                movieListAdapter.setMoviesResponse(s);
                movieListAdapter.setMoviesPosters(NetworkUtilities.getMoviePoster(s));
                showToastMsg(toastMessage);
            } else {
                showErrorMessage();
            }
        }

    }


    //First Startup Async Task
    public class FetchMoviesOnStartUpTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {

            if (NetworkUtilities.isOnlineB()) {

                URL moviesRequest = NetworkUtilities.buildLaunchUrl();

                try {
                    /*String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(moviesRequest);*/

                    return NetworkUtilities.getResponseFromHttpUrl(moviesRequest);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                if (movieListAdapter != null) {
                    removePreviousData();
                }
                return null;

            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.isEmpty()) {
                showMoviesData();
                /*movieResponse = s;*/
                movieListAdapter.setMoviesResponse(s);
                movieListAdapter.setMoviesPosters(NetworkUtilities.getMoviePoster(s));
                showToastMsg(getString(R.string.latest_release));
            } else {
                showErrorMessage();
            }
        }
    }


    //Show and remove toast message when required.
    public void showToastMsg(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);

        mToast.show();
    }

}
