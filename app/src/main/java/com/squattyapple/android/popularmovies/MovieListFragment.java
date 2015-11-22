package com.squattyapple.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final int MOVIE_LOADER = 0;

    private MovieAdapter mMovieAdapter;

    public MovieListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mMovieAdapter = new MovieAdapter(getContext(), 0);

        GridView movieGridView = (GridView)rootView.findViewById(R.id.moviePosterGridView);
        movieGridView.setAdapter(mMovieAdapter);

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), MovieDetailActivity.class).putExtra("Movie", ((Movie) parent.getItemAtPosition(position))));
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    public void onListSettingChanged(){
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mMovieAdapter.clear();
        mMovieAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        mMovieAdapter.clear();
    }
}
