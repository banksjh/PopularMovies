package com.squattyapple.android.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import com.squattyapple.android.popularmovies.TestUtilities;

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testDeleteAll();
    }

    public void testProviderRegistry(){
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(), com.squattyapple.android.popularmovies.data.provider.MovieProvider.class.getName());

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
            " instead of authority: " + MovieProvider.AUTHORITY, MovieProvider.AUTHORITY, providerInfo.authority);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testBasicMovieQueries() {
        ContentValues testValues = new ContentValues();

        testValues.put(FavoriteMovieColumns.BACKDROP_PATH, "/bkg.jpg");
        testValues.put(FavoriteMovieColumns.MOVIE_DB_ID, 123456);
        testValues.put(FavoriteMovieColumns.POSTER_PATH, "/poster.jpg");
        testValues.put(FavoriteMovieColumns.RELEASE_DATE, 1419120000);
        testValues.put(FavoriteMovieColumns.TITLE, "Cool Movie");
        testValues.put(FavoriteMovieColumns.SYNOPSIS, "This is a really cool movie");
        testValues.put(FavoriteMovieColumns.VOTE_AVERAGE, 6.3);

        //Test insert
        Uri movieUri = mContext.getContentResolver().insert(MovieProvider.FavoriteMovies.CONTENT_URI, testValues);
        long movieRowId = ContentUris.parseId(movieUri);

        assertTrue("Error: movie row was not inserted", movieRowId != -1);

        //Test query all movies
        Cursor moviesCursor = mContext.getContentResolver().query(
                MovieProvider.FavoriteMovies.CONTENT_URI,
                null,
                null,
                null,
                null);
        TestUtilities.validateCursor("Error validating query all movies", moviesCursor, testValues);

        //Test query specific movie
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieProvider.FavoriteMovies.withId(123456),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("Error validating query movie with id", movieCursor, testValues);

        assert moviesCursor != null;
        moviesCursor.close();
        assert movieCursor != null;
        movieCursor.close();
    }

    public void testDeleteAll(){
        mContext.getContentResolver().delete(
                MovieProvider.FavoriteMovies.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieProvider.FavoriteMovies.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertNotNull(cursor);
        assertEquals("Error: Records not deleted", 0, cursor.getCount());
    }
}
