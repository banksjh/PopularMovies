package com.squattyapple.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squattyapple.android.popularmovies.data.FavoriteMovieColumns;
import com.squattyapple.android.popularmovies.data.MovieProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private ImageView mPosterImageView;
    private TextView mSynopsisTextView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private Button mMarkAsFavButton;

    Movie mMovie;

    private boolean mIsFavorite = false;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment getInstance(Movie movie){
        MovieDetailFragment instance = new MovieDetailFragment();
        Bundle args = new Bundle();

        args.putParcelable("Movie", movie);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy");
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("Movie")) {
            mMovie = intent.getParcelableExtra("Movie");

            //This is only done in two-pane mode
            ((MovieDetailActivity)getActivity()).setActionBarTitle(mMovie.getTitle());
            ((MovieDetailActivity)getActivity()).setActionBarImageUri(mMovie.getBackdropImageUri());
        } else if (getArguments() != null){
            mMovie = getArguments().getParcelable("Movie");
            Picasso.with(getContext()).load(mMovie.getBackdropImageUri()).into((ImageView)getActivity().findViewById(R.id.movie_backdrop_image));
            ((TextView)getActivity().findViewById(R.id.titleTextView)).setText(mMovie.getTitle());
        } else {
            return;
        }

        mSynopsisTextView.setText(mMovie.getSynopsis());

        mReleaseDateTextView.setText(dateFormatter.format(mMovie.getReleaseDate()));
        mRatingTextView.setText(Double.toString(mMovie.getUserRating()) + "/10");

        Picasso.with(getContext()).load(mMovie.getPosterImageUri()).placeholder(R.mipmap.ic_launcher).into(mPosterImageView);

        Cursor cur = getActivity().getContentResolver().query(MovieProvider.FavoriteMovies.withId(mMovie.getDbId()), new String[]{FavoriteMovieColumns.MOVIE_DB_ID}, null, null, null);
        if (cur != null && cur.getCount() > 0){
            mIsFavorite = true;
            mMarkAsFavButton.setText(R.string.remove_as_fav_btn);
            cur.close();
        }
        RetrieveReviewsTask reviewsTask = new RetrieveReviewsTask();
        reviewsTask.execute();

        RetrieveVideosTask videosTask = new RetrieveVideosTask();
        videosTask.execute();

        getActivity().findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
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

    protected void addReviews(ArrayList<Review> reviews) {
        LinearLayout reviewList = (LinearLayout)getActivity().findViewById(R.id.ratingLinearLayout);

        if (reviews.size() > 0) reviewList.setVisibility(View.VISIBLE);

        for (Review review : reviews){
            View view = getLayoutInflater(null).inflate(R.layout.review_list_item, null);
            ((TextView)view.findViewById(R.id.reviewAuthorTextView)).setText(review.getReviewer());
            ((TextView)view.findViewById(R.id.reviewContentTextView)).setText(review.getReview());

            reviewList.addView(view);
        }
    }

    protected void addVideos(ArrayList<Video> videos){
        LinearLayout videoList = (LinearLayout)getActivity().findViewById(R.id.videoLinearLayout);

        if (videos.size() > 0) videoList.setVisibility(View.VISIBLE);

        for (Video video : videos){
            View view = getLayoutInflater(null).inflate(R.layout.video_list_item, null);
            ((TextView)view.findViewById(R.id.videoTitleTextview)).setText(video.getTitle());
            view.setTag(video.getUrl());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchVideoIntent = new Intent();
                    launchVideoIntent.setData(Uri.parse((String)v.getTag()));

                    startActivity(launchVideoIntent);
                }
            });
            videoList.addView(view);
        }
    }

    private class RetrieveReviewsTask extends AsyncTask<Void, Void, ArrayList<Review>> {

        @Override
        protected ArrayList<Review> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            final String REVIEW_PARAM = "reviews";


            Uri queryUri;

            queryUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                    .appendPath(mMovie.getDbId() + "")
                    .appendPath(REVIEW_PARAM)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();

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
            return Utils.parseReviewsFromJson(jsonStr);
        }

        @Override
        protected void onPostExecute(ArrayList<Review> result){
            addReviews(result);
        }
    }

    private class RetrieveVideosTask extends AsyncTask<Void, Void, ArrayList<Video>>{

        @Override
        protected ArrayList<Video> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            final String VIDEO_PARAM = "videos";


            Uri queryUri;

            queryUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                    .appendPath(mMovie.getDbId() + "")
                    .appendPath(VIDEO_PARAM)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();

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
            return Utils.parseVideosFromJson(jsonStr);
        }

        @Override
        protected void onPostExecute(ArrayList<Video> result){
            addVideos(result);
        }
    }
}
