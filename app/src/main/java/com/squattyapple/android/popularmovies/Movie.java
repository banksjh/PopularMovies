package com.squattyapple.android.popularmovies;

import java.util.Date;

/**
 * Class representing a com.squattyapple.android.popularmovies.Movie
 */
public class Movie {

    public String getImageUri(){
        return mImgUri;
    }
    public void setImageUri(String uri){
        mImgUri = uri;
    }

    public String getTitle(){
        return mTitle;
    }
    public void setTitle(String title){
        mTitle = title;
    }

    public String getSynopsis() {
        return mSynopsis;
    }
    public void setSynopsis(String synopsis) {
        this.mSynopsis = synopsis;
    }

    public float getUserRating() {
        return mUserRating;
    }
    public void setmUserRating(float userRating) {
        this.mUserRating = userRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    private String mImgUri;
    private String mTitle;
    private String mSynopsis;
    private float mUserRating;
    private Date mReleaseDate;
}
