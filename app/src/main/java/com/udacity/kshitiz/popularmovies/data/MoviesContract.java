package com.udacity.kshitiz.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kshitiz on 2/9/2018.
 */

public class MoviesContract {


    public static final String AUTHORITY = "com.udacity.kshitiz.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAV_MOVIES = "movies";
    public static final String PATH_MY_FAV_MOVIES = "my_fav";

    public static final class MoviesEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public static final Uri CONTENT_URI_MY_FAV = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MY_FAV_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_NAME = "movieName";

        public static final String COLUMN_MOVIE_POSTER = "moviePoster";

        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";

        public static final String COLUMN_MOVIE_SYNOPSIS = "movieSynopsis";

        public static final String COLUMN_MOVIE_RATING = "movieRating";

        public static final String COLUMN_MOVIE_ID = "movieID";

        public static final String COLUMN_MOVIES_BY = "moviesBy";

        //Table for favourites

        public static final String TABLE_NAME_FAV = "my_fav";


    }
}
