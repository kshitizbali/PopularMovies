package com.udacity.kshitiz.popularmovies.utilties;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.udacity.kshitiz.popularmovies.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kshitiz on 10/17/2017.
 */

public class ConstantUtilities {


    //THE MOVIE DB API KEY
    final static String MY_MOVIE_DB_API_KEY = BuildConfig.API_KEY;

    //Launch
    final static String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    //Sorting
    final static String BASE_URL_STOCK = "https://api.themoviedb.org/3/movie";

   /* final static String QUERY_PARAM = "q";

    final static String MOVIE_PARAM = "movie";*/

    final public static String POPULAR_PARAM = "popular";

    final public static String KEY = "key";

    final public static String TOP_RATED_PARAM = "top_rated";

    final public static String MOVIES_ON_START_UP = "movies_first_launch";

    final static String API_KEY_PARAM = "api_key";

    final static String LANGUAGE = "language";

    final static String LANGUAGE_SELECTED = "en-US";

    final static String RESULTS = "results";

    final static String IMAGE_PART_URL = "https://image.tmdb.org/t/p/w185/";

    final static String VIDEO_PART_URL = "https://www.youtube.com/watch?v=";

    final static String POSTER_PATH = "poster_path";

    final static String VIDEO_KEY = "key";

    final static String TRAILER_NAME = "name";

    final static String SORT_BY = "sort_by";

    final static String VOTE_COUNT_GTE = "vote_count.gte";

    final public static String VOTE_COUNT_5k = "5000";

    final public static String VOTE_COUNT_RESET = "0";

    final public static String SORT_BY_TOP_RATED = "vote_average.desc";

    final public static String SORT_BY_POPULAR = "popularity.desc";


    final public static String ORIGINAL_TITLE = "original_title";

    final public static String PLOT_SYNOPSIS = "overview";

    final public static String USER_RATING = "vote_average";

    final public static String RELEASE_DATE = "release_date";

    final public static String PRIMARY_RELEASE_DATE_GTE = "primary_release_date.gte";
    final public static String PRIMARY_RELEASE_DATE_LTE = "primary_release_date.lte";


    final public static String INTENT_EXTRA_MOVIE_POSTER = "movie_poster";
    final public static String INTENT_EXTRA_MOVIE_TITLE = "movie_title";
    final public static String INTENT_EXTRA_MOVIE_RATING = "movie_rating";
    final public static String INTENT_EXTRA_MOVIE_SYNOPSIS = "movie_synopsis";
    final public static String INTENT_EXTRA_MOVIE_RELEASE_DATE = "release_date";
    final public static String INTENT_EXTRA_MOVIE_ID = "movie_id";
    final public static String INTENT_EXTRA_FAV_FLAG = "fav-flag";

    final public static int FLAG_FAV = 1;
    final public static int FLAG_NOT_FAV = 0;

    final public static String VIDEOS = "videos";

    final public static String ID = "id";

    final public static String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";


    //get Current Date
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.getTime());
    }

    //get two month prior date from the present date.
    public static String getTwoMonthPriorDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(date);
    }

    public static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }





}
