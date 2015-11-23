package com.squattyapple.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.squattyapple.android.popularmovies.data.FavoriteMovieColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing a com.squattyapple.android.popularmovies.Movie
 */
public class Movie implements Parcelable {

    public Movie(){
    }

    public Movie(Cursor cursor){
        mId = cursor.getLong(cursor.getColumnIndex(FavoriteMovieColumns._ID));
        mDbId = cursor.getLong(cursor.getColumnIndex(FavoriteMovieColumns.MOVIE_DB_ID));
        mPosterImgUri = cursor.getString(cursor.getColumnIndex(FavoriteMovieColumns.POSTER_PATH));
        mBackdropImgUri = cursor.getString(cursor.getColumnIndex(FavoriteMovieColumns.BACKDROP_PATH));
        mTitle = cursor.getString(cursor.getColumnIndex(FavoriteMovieColumns.TITLE));
        mSynopsis = cursor.getString(cursor.getColumnIndex(FavoriteMovieColumns.SYNOPSIS));
        mUserRating = cursor.getDouble(cursor.getColumnIndex(FavoriteMovieColumns.VOTE_AVERAGE));
        mReleaseDate = new Date(cursor.getLong(cursor.getColumnIndex(FavoriteMovieColumns.RELEASE_DATE)));
    }

    private Movie(Parcel in) {
        mId = in.readLong();
        mDbId = in.readLong();
        mPosterImgUri = in.readString();
        mBackdropImgUri = in.readString();
        mTitle = in.readString();
        mSynopsis = in.readString();
        mUserRating = in.readDouble();
        mReleaseDate = new Date(in.readLong());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mDbId);
        dest.writeString(mPosterImgUri);
        dest.writeString(mBackdropImgUri);
        dest.writeString(mTitle);
        dest.writeString(mSynopsis);
        dest.writeDouble(mUserRating);
        dest.writeLong(mReleaseDate.getTime());
    }

    public long getId(){
        return mId;
    }
    public void setId(long id){
        mId = id;
    }
    public long getDbId(){
        return mDbId;
    }
    public void setDbId(long dbId){
        mDbId = dbId;
    }
    public String getPosterImageUri(){
        return mPosterImgUri;
    }
    public void setImageUri(String uri){
        mPosterImgUri = uri;
    }

    public String getBackdropImageUri() { return mBackdropImgUri; }
    public void setBackdropImageUri(String uri) { mBackdropImgUri = uri; }

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

    public double getUserRating() {
        return mUserRating;
    }
    public void setmUserRating(double userRating) {
        this.mUserRating = userRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(FavoriteMovieColumns.BACKDROP_PATH, mBackdropImgUri);
        values.put(FavoriteMovieColumns.MOVIE_DB_ID, mDbId);
        values.put(FavoriteMovieColumns.POSTER_PATH, mPosterImgUri);
        values.put(FavoriteMovieColumns.RELEASE_DATE, mReleaseDate.getTime());
        values.put(FavoriteMovieColumns.TITLE, mTitle);
        values.put(FavoriteMovieColumns.SYNOPSIS, mSynopsis);
        values.put(FavoriteMovieColumns.VOTE_AVERAGE, mUserRating);
        return values;
    }

    private long mId;
    private long mDbId;
    private String mPosterImgUri;
    private String mBackdropImgUri;
    private String mTitle;
    private String mSynopsis;
    private double mUserRating;
    private Date mReleaseDate;
}
