package com.squattyapple.android.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListActivityFragment extends Fragment {

    public MovieListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        String[] imgUris = {"http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png"};

        MovieAdapter adapter = new MovieAdapter(getContext(), 0, imgUris);

        GridView view = (GridView)rootView.findViewById(R.id.moviePosterGridView);
        view.setAdapter(adapter);

        return rootView;
    }
}
