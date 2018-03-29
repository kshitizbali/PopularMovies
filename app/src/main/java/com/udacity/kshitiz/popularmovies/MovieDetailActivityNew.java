package com.udacity.kshitiz.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.kshitiz.popularmovies.adapter.MoviesReviewsAdapter;
import com.udacity.kshitiz.popularmovies.adapter.MoviesVideosAdapter;
import com.udacity.kshitiz.popularmovies.data.MoviesContract;
import com.udacity.kshitiz.popularmovies.utilties.ConstantUtilities;
import com.udacity.kshitiz.popularmovies.utilties.NetworkUtilities;

import java.io.IOException;
import java.net.URL;

public class MovieDetailActivityNew extends AppCompatActivity implements MoviesVideosAdapter.MoviesVideosAdapterOnClickHandler, MoviesReviewsAdapter.MoviesReviewsAdapterOnClickHandler {

    private TextView tvReleaseDate, tvUserRating, tvSynopsis;
    private ImageView ivMoviePoster;
    private String moviePoster, movieRating, movieId;
    private FloatingActionButton fab;
    private RecyclerView rv_movie_trailers;
    private ProgressBar progressBar;
    private TextView tvError;
    private MoviesVideosAdapter moviesVideosAdapter;

    private RecyclerView rv_movie_reviews;
    private MoviesReviewsAdapter moviesReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvUserRating = (TextView) findViewById(R.id.tvUserRating);
        tvSynopsis = (TextView) findViewById(R.id.tvSynopsis);
        rv_movie_trailers = (RecyclerView) findViewById(R.id.rv_movie_trailers);
        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvError = (TextView) findViewById(R.id.tvError);
        rv_movie_reviews = (RecyclerView) findViewById(R.id.rv_movie_reviews);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFav(view);
            }
        });

        rv_movie_trailers.setLayoutManager(new LinearLayoutManager(this));
        moviesVideosAdapter = new MoviesVideosAdapter(this, this);
        rv_movie_trailers.setAdapter(moviesVideosAdapter);

        rv_movie_reviews.setLayoutManager(new LinearLayoutManager(this));
        moviesReviewsAdapter = new MoviesReviewsAdapter(this, this);
        rv_movie_reviews.setAdapter(moviesReviewsAdapter);

        setIntentData();
        new FetchMoviesTrailersTask().execute();
        new FetchMovieReviewsTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Fetches intent data from the parent activity and sets it to textviews and imageview.
    private void setIntentData() {

        Intent intentThatStartedThisActivity = getIntent();


        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_POSTER)) {
                moviePoster = intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_POSTER);
                Picasso.with(MovieDetailActivityNew.this).load(moviePoster).into(ivMoviePoster);
            }
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE)) {

                getSupportActionBar().setTitle(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE));
            }
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE)) {

                tvReleaseDate.setText(String.format(getResources().getString(R.string.two_parameters), getString(R.string.release), intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE)));
                /*tvReleaseDate.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE));*/

            }
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_SYNOPSIS)) {

                tvSynopsis.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_SYNOPSIS));
            }
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING)) {

                movieRating = intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING);

                tvUserRating.setText(String.format(getResources().getString(R.string.ratings_param), movieRating));
                /*tvUserRating.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING));*/

            }

            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_ID)) {

                movieId = intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_ID);
                /*tvUserRating.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING));*/
                if (isFav(movieId)) {
                    fab.setTag(getResources().getString(R.string.fav_on));
                    fab.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_on));
                } else {
                    fab.setTag(getResources().getString(R.string.fav_off));
                    fab.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_off));
                }

            }

        }
    }

    //ConstantUtilities.VIDEO_PART_URL + "" + NetworkUtilities.getJsonValue(jsonResponse,key,pos);

    private void addFav(View view) {


        if (fab.getTag() != null && fab.getTag() == getResources().getString(R.string.fav_off)) {


            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, getSupportActionBar().getTitle().toString().trim());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER, moviePoster);
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING, movieRating);
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, tvReleaseDate.getText().toString().trim());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS, tvSynopsis.getText().toString().trim());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieId);


            Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV, contentValues);

            if (uri != null) {
                Log.i("Insert", "Success");
                Snackbar.make(view, "Saved in favourites.", Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
                fab.setTag(getResources().getString(R.string.fav_on));
                fab.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_on));
            }


        } else if (fab.getTag() != null && fab.getTag() == getResources().getString(R.string.fav_on)) {

            Uri uri = MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV;
            uri = uri.buildUpon().appendEncodedPath(movieId).build();

            int taskDelete = getContentResolver().delete(uri, null, null);

            if (taskDelete != 0) {
                Log.i("Deletion", "Success");
                Snackbar.make(view, "Removed from favourites.", Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
                fab.setTag(getResources().getString(R.string.fav_off));
                fab.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.btn_star_big_off));
            }
        }

        ////UPDATE CODE ////

            /*Uri uri = MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV;
            uri = uri.buildUpon().appendEncodedPath(movieId).build();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_FAV_FLAG, ConzstantUtilities.FLAG_NOT_FAV);

            int taskUpdate = getContentResolver().update(uri, contentValues, null, null);

            if (taskUpdate != 0) {
                Log.i("Updating", "Success");
                ivFav.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
            } else {

            }*/
    }

    private boolean isFav(String movieId) {
        try {
            boolean ifExists;
            Uri uri = MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV;
            uri = uri.buildUpon().appendEncodedPath(movieId).build();

            String[] projection = {MoviesContract.MoviesEntry.COLUMN_MOVIE_ID};
            String selection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " =?";
            String[] selectionArgs = {movieId};

            Cursor cursor = getContentResolver().query(uri, projection, selection, null, null);
            if (cursor.getCount() > 0) {
                ifExists = true;
            } else {
                ifExists = false;
            }
            cursor.close();
            Log.i("Fav", "" + ifExists);
            return ifExists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //shows movies list
    private void showMoviesData() {
        rv_movie_reviews.setVisibility(View.VISIBLE);
        rv_movie_trailers.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.INVISIBLE);
    }

    //shows error messages
    private void showErrorMessage() {
        rv_movie_reviews.setVisibility(View.INVISIBLE);
        rv_movie_trailers.setVisibility(View.INVISIBLE);
        tvError.setVisibility(View.VISIBLE);
    }


    //remove any previous data. In case of no internet connection when we access other api's (it shows error message) but on rotation it shows the previous saved list.
    private void removePreviousData() {
        moviesVideosAdapter.resetMoviesList();
    }

    @Override
    public void onClick(int movieListItemPosition, String moviesResponseList) {


        if (NetworkUtilities.getJsonValue(moviesResponseList, movieListItemPosition, ConstantUtilities.KEY) != null && !NetworkUtilities.getJsonValue(moviesResponseList, movieListItemPosition, ConstantUtilities.KEY).isEmpty()) {
            String key = NetworkUtilities.getJsonValue(moviesResponseList, movieListItemPosition, ConstantUtilities.KEY);
            watchYoutubeVideo(key);
        }


    }

    @Override
    public void onClickShare(int position, String moviesResponseList) {

        if (NetworkUtilities.getJsonValue(moviesResponseList, position, ConstantUtilities.KEY) != null && !NetworkUtilities.getJsonValue(moviesResponseList, position, ConstantUtilities.KEY).isEmpty()) {
            String key = NetworkUtilities.getJsonValue(moviesResponseList, position, ConstantUtilities.KEY);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, Uri.parse("http://www.youtube.com/watch?v=" + key).toString());
            startActivity(Intent.createChooser(intent, "Share"));
        }


    }

    @Override
    public void onClickReviews(int movieListItemPosition, String moviesResponseList) {

        if (NetworkUtilities.getJsonValue(moviesResponseList, movieListItemPosition, ConstantUtilities.URL) != null && !NetworkUtilities.getJsonValue(moviesResponseList, movieListItemPosition, ConstantUtilities.URL).isEmpty()) {

            readMovieReview(NetworkUtilities.getJsonValue(moviesResponseList, movieListItemPosition, ConstantUtilities.URL));
        }
    }


    //First Startup Async Task
    public class FetchMoviesTrailersTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {

            if (NetworkUtilities.isOnlineB()) {

                URL moviesRequest = NetworkUtilities.buildUrlVideos(movieId);

                try {
                    /*String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(moviesRequest);*/


                    return NetworkUtilities.getResponseFromHttpUrl(moviesRequest);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                if (moviesVideosAdapter != null) {
                    removePreviousData();
                }
                return null;

            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.isEmpty()) {
                showMoviesData();
                /*movieResponse = s;*/
                moviesVideosAdapter.setMoviesResponse(s);
                moviesVideosAdapter.setMoviesTrailerName(NetworkUtilities.getTrailerName(s));
            } else {
                showErrorMessage();
            }
        }
    }


    public class FetchMovieReviewsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (NetworkUtilities.isOnlineB()) {

                URL moviesRequest = NetworkUtilities.buildUrlReviews(movieId);

                try {
                    /*String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(moviesRequest);*/


                    return NetworkUtilities.getResponseFromHttpUrl(moviesRequest);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                if (moviesVideosAdapter != null) {
                    removePreviousData();
                }
                return null;

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.isEmpty()) {
                showMoviesData();
                /*movieResponse = s;*/
                moviesReviewsAdapter.setMoviesResponse(s);
                moviesReviewsAdapter.setMoviesReviews(NetworkUtilities.getValueList(s, ConstantUtilities.CONTENT));
                moviesReviewsAdapter.setMoviesReviewsAuthor(NetworkUtilities.getValueList(s, ConstantUtilities.AUTHOR));
            } else {
                showErrorMessage();
            }
        }
    }


    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void readMovieReview(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        try {
            startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
