package com.squattyapple.android.popularmovies;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.*;


public class Utils {

    private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    public static ArrayList<Review> parseReviewsFromJson(String jsonStr){
        ArrayList<Review> reviews = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONObject result = new JSONObject(jsonStr);
                JSONArray reviewArray = result.getJSONArray("results");

                for (int i = 0; i < reviewArray.length(); i++) {
                    JSONObject reviewObject = reviewArray.getJSONObject(i);
                    Review review = new Review(reviewObject.getString("author"), reviewObject.getString("content"));

                    reviews.add(review);
                }

            } catch (JSONException e) {
                Log.e("Utils", "Logged a JSON Exception", e);
            }
        }
        return reviews;
    }

    public static ArrayList<Movie> parseMoviesFromJson (String jsonStr){
        ArrayList<Movie> movies = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONObject result = new JSONObject(jsonStr);

                JSONArray movieArray = result.getJSONArray("results");

                for (int i = 0; i < movieArray.length(); i++) {
                    Movie movie = new Movie();
                    JSONObject jsonMovie = movieArray.getJSONObject(i);

                    movie.setDbId(jsonMovie.getLong("id"));
                    movie.setmUserRating(jsonMovie.getDouble("vote_average"));
                    movie.setSynopsis(jsonMovie.getString("overview"));
                    movie.setTitle(jsonMovie.getString("original_title"));
                    movie.setImageUri("http://image.tmdb.org/t/p/w185/" + jsonMovie.getString("poster_path"));
                    movie.setBackdropImageUri("http://image.tmdb.org/t/p/w780/" + jsonMovie.getString("backdrop_path"));

                    SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
                    movie.setReleaseDate(dateParser.parse(jsonMovie.getString("release_date")));

                    movies.add(movie);
                }
            } catch (JSONException e) {
                Log.e("Utils", "Logged a JSON Exception", e);
            } catch (ParseException e) {
                Log.e("Utils", "Logged a date parser exception", e);
            }
        }

        return movies;
    }

    public static ArrayList<Video> parseVideosFromJson (String jsonStr){
        ArrayList<Video> videos = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONObject result = new JSONObject(jsonStr);
                JSONArray videoArray = result.getJSONArray("results");

                for (int i = 0; i < videoArray.length(); i++) {
                    Video video = new Video();
                    JSONObject jsonVideo = videoArray.getJSONObject(i);
                    if (jsonVideo.getString("site").equals("YouTube")) {
                        video.setTitle(jsonVideo.getString("name"));
                        video.setUrl(YOUTUBE_URL + jsonVideo.getString("key"));

                        videos.add(video);
                    }
                }

            } catch (JSONException e) {
                Log.e("Utils", "Logged a JSON Exception", e);
            }
        }
        return videos;
    }
}
