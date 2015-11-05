package com.squattyapple.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

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

        //TODO: remove this after debugging
        Picasso.with(getContext()).setIndicatorsEnabled(true);
        Picasso.with(getContext()).setLoggingEnabled(true);

        GetMoviesTask loadTask = new GetMoviesTask();
        loadTask.execute(GetMoviesTask.SortOrder.SORT_BY_MOST_POPULAR);


        //placeholder image URIs
        String[] imgUris = {"http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png"};

        MovieAdapter adapter = new MovieAdapter(getContext(), 0, imgUris);

        GridView view = (GridView)rootView.findViewById(R.id.moviePosterGridView);
        view.setAdapter(adapter);

        return rootView;
    }

    private static class GetMoviesTask extends AsyncTask<GetMoviesTask.SortOrder, Void, Void> {
        public final String LOG_TAG = GetMoviesTask.class.getSimpleName();

        @Override
        protected Void doInBackground(SortOrder... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String API_KEY_PARAM = "api_key";
            final String SORT_BY_PARAM = "sort_by";

            String sort = "release_date.desc";

            if (params[0] == SortOrder.SORT_BY_MOST_POPULAR){
                sort = "popularity.desc";
            }

            Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(SORT_BY_PARAM, sort).build();

            try {
                URL url = new URL(builtUri.toString());

                // Create the request to TheMovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
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
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public enum SortOrder{
            SORT_BY_NEWEST, SORT_BY_MOST_POPULAR
        }
    }
}
