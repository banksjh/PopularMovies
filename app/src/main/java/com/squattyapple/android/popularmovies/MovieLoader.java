package com.squattyapple.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.squattyapple.android.popularmovies.data.MovieProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private final String LOG_TAG = MovieLoader.class.getSimpleName();

    private String mSortParam;
    private ArrayList<Movie> mMovies;

    public MovieLoader(Context context) {
        super(context);

        mSortParam = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(
                        context.getString(R.string.pref_sort_key),
                        context.getString(R.string.pref_sort_by_popularity_value));

        mMovies = new ArrayList<>();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        if (mSortParam.equals(getContext().getString(R.string.pref_sort_by_popularity_value)) ||
                mSortParam.equals(getContext().getString(R.string.pref_sort_by_rating_value))) {
            mMovies = loadFromServer();
        } else if (mSortParam.equals(getContext().getString(R.string.pref_sort_by_favorite_value))){
            mMovies = loadFavorites();
        }
        return mMovies;
    }

    @Override
    protected void onStartLoading() {
        String sortParam = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(
                        getContext().getString(R.string.pref_sort_key),
                        getContext().getString(R.string.pref_sort_by_popularity_value));

        //go ahead and deliver what we have
        if (mSortParam.equals(sortParam) && !mMovies.isEmpty()){
            deliverResult(mMovies);
        }
        mSortParam = sortParam;

        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        mMovies.clear();
    }

    private ArrayList<Movie> loadFavorites() {
        ArrayList<Movie> movies = new ArrayList<>();

        Cursor movieCursor = getContext().getContentResolver().query(MovieProvider.FavoriteMovies.CONTENT_URI, null, null, null, null);
        if (movieCursor != null) {
            while (movieCursor.moveToNext()) {
                movies.add(new Movie(movieCursor));
            }
            movieCursor.close();
        }
        return movies;
    }

    private ArrayList<Movie> loadFromServer(){

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

        if (mSortParam.equals(getContext().getString(R.string.pref_sort_by_rating_value))){
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
