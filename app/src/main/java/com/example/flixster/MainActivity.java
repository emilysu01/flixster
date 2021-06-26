package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    List<Movie> movies;

    public static final String  NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=ee61f2cae1dfc55edd134ee4b5da1ed7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate a binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Create a view and set its content
        View view = binding.getRoot();
        setContentView(view);

        // Create empty list of movies
        movies = new ArrayList<>();

        // Create a RecyclerView for movies
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        // Create the MovieAdapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        // Set the MovieAdapter on the RecyclerView
        rvMovies.setAdapter(movieAdapter);
        // Set a LayoutManager on the RecyclerView (required for the RecyclerView to be able to know how to layout the different views onto the screen)
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Make an API call to retrieve movie info
        // Use the JSON response handler because The Movie Database API returns JSON
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("MainActivity", "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    // Parse API call response for movie info
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i("MainActivity", "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i("MainActivity", "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e("MainActivity", "Hit JSON exception", e);
                }
            }
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("MainActivity", "onFailure");
            }
        });
    }
}