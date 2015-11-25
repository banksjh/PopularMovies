package com.squattyapple.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View reviewView;

        if (convertView != null){
            reviewView = convertView;
        } else {
            reviewView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }
        ((TextView)reviewView.findViewById(R.id.reviewAuthorTextView)).setText(getItem(position).getReviewer());
        ((TextView)reviewView.findViewById(R.id.reviewContentTextView)).setText(getItem(position).getReview());

        return reviewView;
    }
}
