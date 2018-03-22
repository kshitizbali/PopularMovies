package com.udacity.kshitiz.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.kshitiz.popularmovies.data.MoviesContract;
import com.udacity.kshitiz.popularmovies.utilties.ConstantUtilities;

/**
 * Created by Kshitiz on 10/23/2017.
 */

public class MovieDetails extends AppCompatActivity {


    private TextView tvMovieName, tvReleaseDate, tvUserRating, tvSynopsis;
    private ImageView ivMoviePoster, ivFav;
    private String moviePoster, movieRating, movieId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        tvMovieName = (TextView) findViewById(R.id.tvMovieName);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvUserRating = (TextView) findViewById(R.id.tvUserRating);
        tvSynopsis = (TextView) findViewById(R.id.tvSynopsis);

        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        ivFav = (ImageView) findViewById(R.id.ivFav);

        setIntentData();

        /*Toast.makeText(MovieDetails.this, "Current Date " + ConstantUtilities.getCurrentDate(), Toast.LENGTH_LONG).show();
        Toast.makeText(MovieDetails.this, "2 months ago date " + ConstantUtilities.getMonthPriorDate(), Toast.LENGTH_LONG).show();*/

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
                Picasso.with(MovieDetails.this).load(moviePoster).into(ivMoviePoster);
            }
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE)) {

                tvMovieName.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE));
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

            }
        }
    }

    //ConstantUtilities.VIDEO_PART_URL + "" + NetworkUtilities.getJsonValue(jsonResponse,key,pos);

    private void addFav() {
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ivFav.getDrawable() != null && ivFav.getDrawable() == getResources().getDrawable(android.R.drawable.btn_star_big_off)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, tvMovieName.getText().toString().trim());
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER, moviePoster);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING, movieRating);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, tvReleaseDate.getText().toString().trim());
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS, tvSynopsis.getText().toString().trim());
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieId);


                    Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV, contentValues);

                    if (uri != null) {
                        Log.i("Insert", "Success");
                        ivFav.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    } /*else {
                        Log.i("Insert", "Failed");
                        ivFav.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    }*/
                } else {


                    Uri uri = MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV;
                    uri = uri.buildUpon().appendEncodedPath(movieId).build();

                    int taskDelete = getContentResolver().delete(uri, null, null);

                    if (taskDelete != 0) {
                        Log.i("Deletion", "Success");
                        ivFav.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    }

                    ////UPDATE CODE ////

                    /*Uri uri = MoviesContract.MoviesEntry.CONTENT_URI_MY_FAV;
                    uri = uri.buildUpon().appendEncodedPath(movieId).build();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_FAV_FLAG, ConstantUtilities.FLAG_NOT_FAV);

                    int taskUpdate = getContentResolver().update(uri, contentValues, null, null);

                    if (taskUpdate != 0) {
                        Log.i("Updating", "Success");
                        ivFav.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    }*//*else {

                    }*/
                }

            }
        });
    }

}
