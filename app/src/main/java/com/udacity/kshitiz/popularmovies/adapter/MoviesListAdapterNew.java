package com.udacity.kshitiz.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.kshitiz.popularmovies.MainActivityNew;
import com.udacity.kshitiz.popularmovies.R;

/**
 * Created by Kshitiz on 3/2/2018.
 */

public class MoviesListAdapterNew extends RecyclerView.Adapter<MoviesListAdapterNew.MoviesListAdapterNewViewHolder> {

    //private String[] mMovieList;
    private Context mContext;
    private final MoviesListAdapterNew.MovieListAdapterOnClickHandler movieListAdapterOnClickHandler;
    //private String moviesResponse;
    private Cursor mCursor;


    public MoviesListAdapterNew(Context context, MoviesListAdapterNew.MovieListAdapterOnClickHandler movieListAdapterOnClickHandler) {
        mContext = context;
        this.movieListAdapterOnClickHandler = movieListAdapterOnClickHandler;

    }

    public interface MovieListAdapterOnClickHandler {
        void onClick(int movieListItemPosition, Cursor cursor);
    }

    @Override
    public MoviesListAdapterNew.MoviesListAdapterNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        view.setFocusable(true);
        return new MoviesListAdapterNew.MoviesListAdapterNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesListAdapterNew.MoviesListAdapterNewViewHolder holder, int position) {
        /*String movieItem = mMovieList[position];
        Picasso.with(mContext).load(movieItem).into(holder.iv_Movie_Poster);*/

        mCursor.moveToPosition(position);

        String moviePoster = mCursor.getString(MainActivityNew.INDEX_MOVIE_POSTER);

        Picasso.with(mContext).load(moviePoster).into(holder.iv_Movie_Poster);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public class MoviesListAdapterNewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iv_Movie_Poster;

        MoviesListAdapterNewViewHolder(View itemView) {
            super(itemView);
            iv_Movie_Poster = (ImageView) itemView.findViewById(R.id.iv_Movie_Poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            //String movieItem = mMovieList[adapterPosition];
            movieListAdapterOnClickHandler.onClick(adapterPosition, mCursor);
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    //set movies posters string array. @param moviesData : movies posters string array.
    /*public void setMoviesPosters(String[] moviesData) {
        mMovieList = moviesData;
        notifyDataSetChanged();
    }


    //set movies json response. @param movieResponse : json response from the server.
    public void setMoviesResponse(String moviesResponse) {
        this.moviesResponse = moviesResponse;
        notifyDataSetChanged();
    }


    //fetch movies posters string array
    public String[] getMoviePosters() {
        if (mMovieList != null && mMovieList.length != 0) {
            return mMovieList;
        } else {
            return null;
        }
    }

    //fetch movies list json response
    public String getMoviesResponse() {
        if (moviesResponse != null && moviesResponse.length() != 0)

            return moviesResponse;

        else
            return null;
    }


    //reset or remove item from movieList Array and reset movie response string.
    public void resetMoviesList() {

        if (moviesResponse != null && mMovieList != null) {
            moviesResponse = "";
            mMovieList = new String[0];
        }
    }*/
}
