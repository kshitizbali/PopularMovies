package com.udacity.kshitiz.popularmovies.utilties;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Kshitiz on 10/17/2017.
 */

public class NetworkUtilities {

    private static final String TAG = NetworkUtilities.class.getSimpleName();


    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    //Checks for a working internet connection
    public static boolean isOnlineB() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Builds the URL used to talk to the server
     *
     * @param sortBy    The sort order that will be queried for.
     * @param voteCount number of votes (top rated movies).
     * @return The URL to use to query the  server.
     */
    public static URL buildUrl(String sortBy, String voteCount) {
        Uri builtUri = Uri.parse(ConstantUtilities.BASE_URL).buildUpon()
                /*.appendPath(moviesBy)*/
                .appendQueryParameter(ConstantUtilities.API_KEY_PARAM, ConstantUtilities.MY_MOVIE_DB_API_KEY)
                .appendQueryParameter(ConstantUtilities.LANGUAGE, ConstantUtilities.LANGUAGE_SELECTED)
                .appendQueryParameter(ConstantUtilities.SORT_BY, sortBy)
                .appendQueryParameter(ConstantUtilities.VOTE_COUNT_GTE, voteCount)
                /*.appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))*/
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * Builds the URL used to talk to the server
     *
     * @param sortBy The sort order that will be queried for.
     * @return The URL to use to query the  server.
     */
    public static URL buildUrlStock(String sortBy) {
        Uri builtUri = Uri.parse(ConstantUtilities.BASE_URL_STOCK).buildUpon()
                /*.appendPath(moviesBy)*/
                .appendQueryParameter(ConstantUtilities.API_KEY_PARAM, ConstantUtilities.MY_MOVIE_DB_API_KEY)
                .appendEncodedPath(sortBy)
                /*.appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))*/
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built STOCK URI " + url);

        return url;
    }


    /**
     * Builds the URL used to talk to the server on app first launch
     *
     * @return The URL to use to query the  server.
     */
    public static URL buildLaunchUrl() {
        Uri builtUri = Uri.parse(ConstantUtilities.BASE_URL).buildUpon()
                /*.appendPath(moviesBy)*/
                .appendQueryParameter(ConstantUtilities.API_KEY_PARAM, ConstantUtilities.MY_MOVIE_DB_API_KEY)
                .appendQueryParameter(ConstantUtilities.LANGUAGE, ConstantUtilities.LANGUAGE_SELECTED)
                .appendQueryParameter(ConstantUtilities.PRIMARY_RELEASE_DATE_GTE, ConstantUtilities.getTwoMonthPriorDate())
                .appendQueryParameter(ConstantUtilities.PRIMARY_RELEASE_DATE_LTE, ConstantUtilities.getCurrentDate())
                /*.appendQueryParameter(ConstantUtilities.SORT_BY, sortBy)
                .appendQueryParameter(ConstantUtilities.VOTE_COUNT_GTE, voteCount)*/
                /*.appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))*/
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    /**
     * This method returns the string array of movie posters parsed from the main json HTTP response.
     *
     * @param jsonResponse The HTTP response.
     * @return string array of movie posters parsed from the main json HTTP response.
     */
    public static String[] getMoviePoster(String jsonResponse) {

        try {
            JSONObject completeJsonObject = new JSONObject(jsonResponse);
            JSONArray resultsJsonArray = completeJsonObject.getJSONArray(ConstantUtilities.RESULTS);

            String[] moviePosters = new String[resultsJsonArray.length()];

            for (int i = 0; i < resultsJsonArray.length(); i++) {

                JSONObject movieItem = resultsJsonArray.getJSONObject(i);

                moviePosters[i] = ConstantUtilities.IMAGE_PART_URL + "" + movieItem.getString(ConstantUtilities.POSTER_PATH);
            }

            return moviePosters;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }


    }

    /**
     * This method returns the a particular json values parsed from the main json HTTP response. for ex title, rating etc
     *
     * @param jsonResponse The HTTP response.
     * @param position     position of the item in the json array.
     * @param jsonKey      key of the item to be extracted from the json response.
     * @return string array of movie posters parsed from the main json HTTP response.
     */
    public static String getJsonValue(String jsonResponse, int position, String jsonKey) {


        try {
            JSONObject completeJsonObject = new JSONObject(jsonResponse);
            JSONArray resultsJsonArray = completeJsonObject.getJSONArray(ConstantUtilities.RESULTS);
            JSONObject movieItem = resultsJsonArray.getJSONObject(position);

            return movieItem.getString(jsonKey);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }


}
