package com.squattyapple.android.popularmovies;

import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MovieAdapter(Context context, int resource, Movie[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View movieView;

        if (null != convertView){
            movieView = convertView;
        }else {
                movieView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, parent, false);
        }
        ViewHolder holder = new ViewHolder(movieView);
        movieView.setTag(holder);

        Picasso.with(getContext()).load(getItem(position).getImageUri()).placeholder(R.mipmap.ic_launcher).into(holder.posterView);
        holder.ratingView.setText(String.format("%.1f", getItem(position).getUserRating()));
        holder.favoriteView.setVisibility(View.INVISIBLE);

        return movieView;
    }

    public static class ViewHolder {
        public final ImageView posterView;
        public final ImageView favoriteView;
        public final TextView ratingView;

        public ViewHolder(View view){
            posterView = (ImageView)view.findViewById(R.id.moviePosterImageView);
            favoriteView = (ImageView)view.findViewById(R.id.favoriteMovieImgView);
            ratingView = (TextView)view.findViewById(R.id.movieRatingTextView);
        }
    }
}
