package com.squattyapple.android.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;

import net.simonvt.schematic.annotation.*;

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {

    public static final String AUTHORITY = "com.squattyapple.android.popularmovies.MovieProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String FAVORITE_MOVIES = "favorite_movies";
    }

    @TableEndpoint(table = MovieDatabase.FAVORITE_MOVIES)
    public static class FavoriteMovies {
        @ContentUri(
                path = Path.FAVORITE_MOVIES,
                type = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + Path.FAVORITE_MOVIES,
                defaultSort = FavoriteMovieColumns.TITLE + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(Path.FAVORITE_MOVIES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.FAVORITE_MOVIES + "/#",
                type = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + Path.FAVORITE_MOVIES,
                whereColumn = FavoriteMovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.FAVORITE_MOVIES, String.valueOf(id));
        }
    }
}
