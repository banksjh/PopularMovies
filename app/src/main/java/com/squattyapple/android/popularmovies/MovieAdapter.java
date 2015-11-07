package com.squattyapple.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MovieAdapter(Context context, int resource, Movie[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgView;

        if (null != convertView && convertView instanceof ImageView){
            imgView = (ImageView)convertView;
        }else {
            imgView = new ImageView(getContext());
        }
        imgView.setAdjustViewBounds(true);
        imgView.setMaxWidth(10);

        Picasso.with(getContext()).load(getItem(position).getImageUri()).placeholder(R.mipmap.ic_launcher).into(imgView);
        return imgView;
    }
}
