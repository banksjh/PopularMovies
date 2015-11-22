package com.squattyapple.android.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;


public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final MovieListFragment listActivityFragment = (MovieListFragment)getSupportFragmentManager().findFragmentById(R.id.movie_list_fragment);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final List<String> sortStrings = Arrays.asList(getResources().getStringArray(R.array.pref_sort_list_values));
        Spinner sortSpinner = (Spinner)toolbar.findViewById(R.id.sort_spinner);
        String sortValue = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_by_popularity_value));

        int sortIndex = sortStrings.indexOf(sortValue);
        sortSpinner.setSelection(sortIndex);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putString(getString(R.string.pref_sort_key), sortStrings.get(position)).commit();
                listActivityFragment.onListSettingChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
