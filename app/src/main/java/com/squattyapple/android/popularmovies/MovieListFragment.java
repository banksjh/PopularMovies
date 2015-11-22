package com.squattyapple.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {

    public MovieListFragment() {
    }

    private MovieAdapter mMovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mMovieAdapter = new MovieAdapter(getContext(), 0);

        GridView movieGridView = (GridView)rootView.findViewById(R.id.moviePosterGridView);
        movieGridView.setAdapter(mMovieAdapter);

        //refreshMovies();

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), MovieDetailActivity.class).putExtra("Movie", ((Movie) parent.getItemAtPosition(position))));
            }
        });

        return rootView;
    }

    public void onListSettingChanged(){
        refreshMovies();
    }

    private void refreshMovies(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        GetMoviesTask loadTask = new GetMoviesTask();
        loadTask.execute(prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_by_popularity_value)));
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshMovies();
    }

    //AsyncTask to load movie data in the background
    private class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String LOG_TAG = GetMoviesTask.class.getSimpleName();

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mMovieAdapter.clear();
            mMovieAdapter.addAll(movies);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String API_KEY_PARAM = "api_key";
            final String SORT_BY_PARAM = "sort_by";
            final String SORT_BY_RATING = "vote_average.desc";
            final String SORT_BY_POPULARITY = "popularity.desc";
            final String MINIMUM_RATING_COUNT = "vote_count.gte";

            Uri queryUri;

            if (params[0].equals(getString(R.string.pref_sort_by_rating_value))){
                queryUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(SORT_BY_PARAM, SORT_BY_RATING)
                        .appendQueryParameter(MINIMUM_RATING_COUNT, "5").build();

            } else {
                queryUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(SORT_BY_PARAM, SORT_BY_POPULARITY).build();
            }

            try {
                URL url = new URL(queryUri.toString());

                // Create the request to TheMovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line);
                    buffer.append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                jsonStr = buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return Utils.parseMoviesFromJson(jsonStr);
        }
    }
}