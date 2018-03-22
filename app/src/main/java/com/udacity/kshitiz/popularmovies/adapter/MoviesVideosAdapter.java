package com.udacity.kshitiz.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.kshitiz.popularmovies.R;

/**
 * Created by Kshitiz on 3/15/2018.
 */

public class MoviesVideosAdapter extends RecyclerView.Adapter<MoviesVideosAdapter.MoviesVideosAdapterViewHolder> {

    private Context mContext;
    private String[] mMovieTrailersList;
    private final MoviesVideosAdapterOnClickHandler moviesVideosAdapterOnClickHandler;
    private String moviesTrailerResponse;

    public MoviesVideosAdapter(Context context, MoviesVideosAdapterOnClickHandler onClickHandler) {
        this.mContext = context;
        this.moviesVideosAdapterOnClickHandler = onClickHandler;
    }


    @Override
    public MoviesVideosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutId = R.layout.movie_trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);

        return new MoviesVideosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesVideosAdapterViewHolder holder, int position) {

        String movieTrailerItem = mMovieTrailersList[position];
        holder.tvTrailerName.setText(movieTrailerItem);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieTrailersList) return 0;
        else return mMovieTrailersList.length;
    }

    public interface MoviesVideosAdapterOnClickHandler {
        void onClick(int movieListItemPosition, String moviesResponseList);

        void onClickShare(int position, String moviesResponseList);
    }


    public class MoviesVideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvTrailerName;
        final ImageView ivShare;

        public MoviesVideosAdapterViewHolder(View itemView) {
            super(itemView);

            tvTrailerName = (TextView) itemView.findViewById(R.id.tvName);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            itemView.setOnClickListener(this);
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    moviesVideosAdapterOnClickHandler.onClickShare(adapterPosition, moviesTrailerResponse);
                }
            });
        }

        @Override
        public void onClick(View v) {


            int adapterPosition = getAdapterPosition();
            //String movieItem = mMovieList[adapterPosition];
            moviesVideosAdapterOnClickHandler.onClick(adapterPosition, moviesTrailerResponse);

        }
    }


    //set movies posters string array. @param moviesData : movies posters string array.
    public void setMoviesTrailerName(String[] moviesData) {
        mMovieTrailersList = moviesData;
        notifyDataSetChanged();
    }


    //set movies json response. @param movieResponse : json response from the server.
    public void setMoviesResponse(String moviesResponse) {
        this.moviesTrailerResponse = moviesResponse;
        notifyDataSetChanged();
    }


    //fetch movies posters string array
    public String[] getMovieTrailerNames() {
        if (mMovieTrailersList != null && mMovieTrailersList.length != 0) {
            return mMovieTrailersList;
        } else {
            return null;
        }
    }

    //fetch movies list json response
    public String getMoviesTrailerResponse() {
        if (moviesTrailerResponse != null && moviesTrailerResponse.length() != 0)

            return moviesTrailerResponse;

        else
            return null;
    }


    //reset or remove item from movieList Array and reset movie response string.
    public void resetMoviesList() {

        if (moviesTrailerResponse != null && mMovieTrailersList != null) {
            moviesTrailerResponse = "";
            mMovieTrailersList = new String[0];
        }
    }


}
