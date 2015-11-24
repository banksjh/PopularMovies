package com.squattyapple.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squattyapple.android.popularmovies.data.FavoriteMovieColumns;
import com.squattyapple.android.popularmovies.data.MovieProvider;

import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    ImageView mPosterImageView;
    TextView mSynopsisTextView;
    TextView mReleaseDateTextView;
    TextView mRatingTextView;
    Button mMarkAsFavButton;

    Movie mMovie;

    private boolean mIsFavorite = false;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy");
        Intent intent = getActivity().getIntent();
        if (intent != null){
            mMovie = intent.getParcelableExtra("Movie");

            mSynopsisTextView.setText(mMovie.getSynopsis());

            mReleaseDateTextView.setText(dateFormatter.format(mMovie.getReleaseDate()));
            mRatingTextView.setText(Double.toString(mMovie.getUserRating()) + "/10");

            Picasso.with(getContext()).load(mMovie.getPosterImageUri()).placeholder(R.mipmap.ic_launcher).into(mPosterImageView);

            ((MovieDetailActivity)getActivity()).setActionBarTitle(mMovie.getTitle());
            ((MovieDetailActivity)getActivity()).setActionBarImageUri(mMovie.getBackdropImageUri());

            Cursor cur = getActivity().getContentResolver().query(MovieProvider.FavoriteMovies.withId(mMovie.getDbId()), new String[]{FavoriteMovieColumns.MOVIE_DB_ID}, null, null, null);
            if (cur != null && cur.getCount() > 0){
                mIsFavorite = true;
                mMarkAsFavButton.setText(R.string.remove_as_fav_btn);
            }
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
        mMarkAsFavButton = ((Button)rootView.findViewById(R.id.markAsFavButton));

        mMarkAsFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFavorite) {
                    getActivity().getContentResolver().delete(MovieProvider.FavoriteMovies.withId(mMovie.getDbId()), null, null);
                    mMarkAsFavButton.setText(R.string.mark_as_fav_btn);
                    mIsFavorite = false;
                } else {
                    ContentValues values = mMovie.getContentValues();
                    getActivity().getContentResolver().insert(MovieProvider.FavoriteMovies.CONTENT_URI, values);
                    mMarkAsFavButton.setText(R.string.remove_as_fav_btn);
                    mIsFavorite = true;
                }
            }
        });
        return rootView;
    }
}
