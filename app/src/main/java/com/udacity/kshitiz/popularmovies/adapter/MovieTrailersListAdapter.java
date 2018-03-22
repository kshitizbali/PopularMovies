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
 * Created by Kshitiz on 2/21/2018.
 */

public class MovieTrailersListAdapter extends RecyclerView.Adapter<MovieTrailersListAdapter.MovieTrailersListAdapterViewHolder> {

    private String[] mTrailersList;
    private Context context;
    private final MovieTrailersListAdapterOnClickHandler movieTrailersListAdapterOnClickHandler;
    private String trailerResponse;


    public MovieTrailersListAdapter(MovieTrailersListAdapterOnClickHandler movieTrailersListAdapterOnClickHandler) {
        this.movieTrailersListAdapterOnClickHandler = movieTrailersListAdapterOnClickHandler;
    }

    @Override
    public MovieTrailersListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new MovieTrailersListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailersListAdapterViewHolder holder, int position) {

        String trailerItem = mTrailersList[position];
        holder.tvName.setText(trailerItem);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailersList) return 0;
        return mTrailersList.length;
    }

    public class MovieTrailersListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView ivPlay, ivShare;
        public final TextView tvName;

        public MovieTrailersListAdapterViewHolder(View itemView) {
            super(itemView);
            ivPlay = (ImageView) itemView.findViewById(R.id.ivPlay);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            movieTrailersListAdapterOnClickHandler.onClick(adapterPos, mTrailersList[adapterPos], trailerResponse);

        }
    }

    public interface MovieTrailersListAdapterOnClickHandler {
        void onClick(int movieTrailerListItemPos, String shareLink, String jsonResponse);
    }

    public void setTrailersName(String[] trailersData) {
        mTrailersList = trailersData;
        notifyDataSetChanged();
    }

    public void setTrailersResponse(String trailerResponse) {
        this.trailerResponse = trailerResponse;
        notifyDataSetChanged();
    }

    public String[] getTrailerNames() {
        if (mTrailersList != null && mTrailersList.length != 0) {
            return mTrailersList;
        } else {
            return null;
        }
    }

    public String getTrailerResponse() {
        if (trailerResponse != null && trailerResponse.length() != 0)

            return trailerResponse;

        else
            return null;
    }

    public void resetTrailerList() {
        if (trailerResponse != null && mTrailersList != null) {
            trailerResponse = "";
            mTrailersList = new String[0];
        }
    }
}
