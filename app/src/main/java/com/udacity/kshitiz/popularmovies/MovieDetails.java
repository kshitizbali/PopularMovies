package com.udacity.kshitiz.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.kshitiz.popularmovies.utilties.ConstantUtilities;

/**
 * Created by Kshitiz on 10/23/2017.
 */

public class MovieDetails extends AppCompatActivity {


    private TextView tvMovieName, tvReleaseDate, tvUserRating, tvSynopsis;
    private ImageView ivMoviePoster;


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
                Picasso.with(MovieDetails.this).load(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_POSTER)).into(ivMoviePoster);
            }
        }

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE)) {

                tvMovieName.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_TITLE));
            }
        }

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE)) {

                tvReleaseDate.setText(String.format(getResources().getString(R.string.two_parameters), getString(R.string.release), intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE)));
                /*tvReleaseDate.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RELEASE_DATE));*/

            }
        }

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_SYNOPSIS)) {

                tvSynopsis.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_SYNOPSIS));
            }
        }

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING)) {

                tvUserRating.setText(String.format(getResources().getString(R.string.ratings_param), intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING)));
                /*tvUserRating.setText(intentThatStartedThisActivity.getStringExtra(ConstantUtilities.INTENT_EXTRA_MOVIE_RATING));*/

            }
        }

    }


}
