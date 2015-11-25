package com.squattyapple.android.popularmovies;

public class Review {
    private String mReview;
    private String mReviewer;

    public Review (String reviewer, String review){
        mReview = review;
        mReviewer = reviewer;
    }

    public void setReview(String review){
        mReview = review;
    }
    public String getReview(){
        return mReview;
    }

    public void setReviewer(String reviewer){
        mReviewer = reviewer;
    }
    public String getReviewer(){
        return mReviewer;
    }
}

