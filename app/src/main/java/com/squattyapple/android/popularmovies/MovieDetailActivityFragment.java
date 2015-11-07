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

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ImageView imgView = (ImageView)rootView.findViewById(R.id.posterImageView);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy");
        Intent intent = getActivity().getIntent();
        if (intent != null){
            Movie movie = intent.getParcelableExtra("Movie");

            ((TextView)rootView.findViewById(R.id.titleTextView)).setText(movie.getTitle());
            ((TextView)rootView.findViewById(R.id.synopsisTextView)).setText(movie.getSynopsis());



            ((TextView)rootView.findViewById(R.id.releaseDateTextView)).setText(dateFormatter.format(movie.getReleaseDate()));
            ((TextView)rootView.findViewById(R.id.ratingTextView)).setText(Double.toString(movie.getUserRating()) + "/10");

            Picasso.with(getContext()).load(movie.getImageUri()).placeholder(R.mipmap.ic_launcher).into(imgView);
        }
        return rootView;
    }
}
