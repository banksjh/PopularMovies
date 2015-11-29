package com.squattyapple.android.popularmovies;

public class Video {
    private String mTitle;
    private String mUrl;

    public String getTitle(){
        return mTitle;
    }
    public void setTitle(String title){
        mTitle = title;
    }

    public String getUrl(){
        return mUrl;
    }
    public void setUrl(String url){
        mUrl = url;
    }
}
