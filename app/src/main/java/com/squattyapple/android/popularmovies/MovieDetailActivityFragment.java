package com.squattyapple.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    ImageView mPosterImageView;
    TextView mSynopsisTextView;
    TextView mReleaseDateTextView;
    TextView mRatingTextView;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy");
        Intent intent = getActivity().getIntent();
        if (intent != null){
            Movie movie = intent.getParcelableExtra("Movie");

            mSynopsisTextView.setText(movie.getSynopsis());



            mReleaseDateTextView.setText(dateFormatter.format(movie.getReleaseDate()));
            mRatingTextView.setText(Double.toString(movie.getUserRating()) + "/10");

            Picasso.with(getContext()).load(movie.getPosterImageUri()).placeholder(R.mipmap.ic_launcher).into(mPosterImageView);

            ((MovieDetailActivity)getActivity()).setActionBarTitle(movie.getTitle());
            ((MovieDetailActivity)getActivity()).setActionBarImageUri(movie.getBackdropImageUri());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mSynopsisTextView = ((TextView)rootView.findViewById(R.id.synopsisTextView));
        mReleaseDateTextView = ((TextView)rootView.findViewById(R.id.releaseDateTextView));
        mRatingTextView = ((TextView)rootView.findViewById(R.id.ratingTextView));
        mPosterImageView = ((ImageView)rootView.findViewById(R.id.posterImageView));

        return rootView;
    }
}
