package com.udacity.kshitiz.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.kshitiz.popularmovies.R;

/**
 * Created by Kshitiz on 3/23/2018.
 */

public class MoviesReviewsAdapter extends RecyclerView.Adapter<MoviesReviewsAdapter.MoviesReviewsAdapterViewHolder> {

    private Context mContext;
    private String[] mMovieReviewsList;
    private String[] mMovieReviewsAuthor;
    private final MoviesReviewsAdapterOnClickHandler moviesReviewsAdapterOnClickHandler;
    private String moviesReviewsResponse;

    public MoviesReviewsAdapter(Context context, MoviesReviewsAdapterOnClickHandler onClickHandler) {
        this.mContext = context;
        this.moviesReviewsAdapterOnClickHandler = onClickHandler;
    }


    @Override
    public MoviesReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutId = R.layout.movies_reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);

        return new MoviesReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesReviewsAdapterViewHolder holder, int position) {

        String tvAuthor = mMovieReviewsAuthor[position];
        String tvReviewContent = mMovieReviewsList[position];
        holder.tvAuthor.setText(tvAuthor);
        holder.tvReviewContent.setText(tvReviewContent);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieReviewsList) return 0;
        else return mMovieReviewsList.length;
    }

    public interface MoviesReviewsAdapterOnClickHandler {
        void onClickReviews(int movieListItemPosition, String moviesResponseList);
    }


    public class MoviesReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvAuthor, tvReviewContent;

        public MoviesReviewsAdapterViewHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvReviewContent = (TextView) itemView.findViewById(R.id.tvReviewContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            int adapterPosition = getAdapterPosition();
            //String movieItem = mMovieList[adapterPosition];
            moviesReviewsAdapterOnClickHandler.onClickReviews(adapterPosition, moviesReviewsResponse);

        }
    }


    //set movies posters string array. @param moviesData : movies posters string array.
    public void setMoviesReviews(String[] moviesData) {
        mMovieReviewsList = moviesData;
        notifyDataSetChanged();
    }

    public void setMoviesReviewsAuthor(String[] moviesData) {
        mMovieReviewsAuthor = moviesData;
        notifyDataSetChanged();
    }


    //set movies json response. @param movieResponse : json response from the server.
    public void setMoviesResponse(String moviesResponse) {
        this.moviesReviewsResponse = moviesResponse;
        notifyDataSetChanged();
    }


    //fetch movies posters string array
    public String[] getMovieReviews() {
        if (mMovieReviewsList != null && mMovieReviewsList.length != 0) {
            return mMovieReviewsList;
        } else {
            return null;
        }
    }

    public String[] getMoviesReviewsAuthor() {
        if (mMovieReviewsAuthor != null && mMovieReviewsAuthor.length != 0) {
            return mMovieReviewsAuthor;
        } else {
            return null;
        }
    }

    //fetch movies list json response
    public String getMoviesReviewsResponse() {
        if (moviesReviewsResponse != null && moviesReviewsResponse.length() != 0)

            return moviesReviewsResponse;

        else
            return null;
    }


    //reset or remove item from movieList Array and reset movie response string.
    public void resetMoviesList() {

        if (moviesReviewsResponse != null && mMovieReviewsList != null && mMovieReviewsAuthor != null) {
            moviesReviewsResponse = "";
            mMovieReviewsList = new String[0];
            mMovieReviewsAuthor = new String[0];
        }
    }
}
