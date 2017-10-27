package com.udacity.kshitiz.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.kshitiz.popularmovies.R;

/**
 * Created by Kshitiz on 10/17/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviesListAdapterViewHolder> {

    private String[] mMovieList;
    private Context context;
    private final MovieListAdapterOnClickHandler movieListAdapterOnClickHandler;
    private String moviesResponse;


    public MovieListAdapter(MovieListAdapterOnClickHandler movieListAdapterOnClickHandler) {
        this.movieListAdapterOnClickHandler = movieListAdapterOnClickHandler;

    }

    public interface MovieListAdapterOnClickHandler {
        void onClick(int movieListItemPosition, String moviePoster, String moviesList);
    }

    @Override
    public MoviesListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new MoviesListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesListAdapterViewHolder holder, int position) {
        String movieItem = mMovieList[position];
        Picasso.with(context).load(movieItem).into(holder.iv_Movie_Poster);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieList) return 0;
        return mMovieList.length;
    }

    public class MoviesListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView iv_Movie_Poster;

        public MoviesListAdapterViewHolder(View itemView) {
            super(itemView);
            iv_Movie_Poster = (ImageView) itemView.findViewById(R.id.iv_Movie_Poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            //String movieItem = mMovieList[adapterPosition];
            movieListAdapterOnClickHandler.onClick(adapterPosition, mMovieList[adapterPosition], moviesResponse);
        }
    }

    //set movies posters string array. @param moviesData : movies posters string array.
    public void setMoviesPosters(String[] moviesData) {
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
    }
}
