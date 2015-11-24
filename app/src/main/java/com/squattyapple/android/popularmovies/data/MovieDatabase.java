package com.squattyapple.android.popularmovies.data;

import net.simonvt.schematic.annotation.*;

@Database(version = MovieDatabase.VERSION, packageName = "com.squattyapple.android.popularmovies.data.provider")
public final class MovieDatabase {
    public static final int VERSION = 1;

    @Table(FavoriteMovieColumns.class)
    public static final String FAVORITE_MOVIES = "favorite_movies";
}
