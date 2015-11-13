package com.squattyapple.android.popularmovies.data;

import net.simonvt.schematic.annotation.*;


public interface FavoriteMovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
    String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @NotNull @Unique
    String MOVIE_DB_ID = "movie_id_name";
    @DataType(DataType.Type.TEXT) @NotNull
    String TITLE = "title";
    @DataType(DataType.Type.INTEGER) @NotNull
    String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.REAL) @NotNull
    String VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.TEXT) @NotNull
    String SYNOPSIS = "synopsis";
    @DataType(DataType.Type.TEXT) @NotNull
    String POSTER_PATH = "poster_path";
    @DataType(DataType.Type.TEXT) @NotNull
    String BACKDROP_PATH = "backdrop_path";
}
