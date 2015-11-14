package com.squattyapple.android.popularmovies;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.*;


public class Utils {

    public static ArrayList<Movie> parseMoviesFromJson (String jsonStr){
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject result = new JSONObject(jsonStr);

            JSONArray movieArray = result.getJSONArray("results");

            for (int i = 0; i < movieArray.length(); i++){
                Movie movie = new Movie();
                JSONObject jsonMovie = movieArray.getJSONObject(i);

                movie.setmUserRating(jsonMovie.getDouble("vote_average"));
                movie.setSynopsis(jsonMovie.getString("overview"));
                movie.setTitle(jsonMovie.getString("original_title"));
                movie.setImageUri("http://image.tmdb.org/t/p/w185/" + jsonMovie.getString("poster_path"));

                SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
                movie.setReleaseDate(dateParser.parse(jsonMovie.getString("release_date")));

                movies.add(movie);
            }
        } catch (JSONException e){
            Log.e("Utils", "Logged a JSON Exception", e);
        } catch (ParseException e) {
            Log.e("Utils", "Logged a date parser exception", e);
        }

        return movies;
    }
}
