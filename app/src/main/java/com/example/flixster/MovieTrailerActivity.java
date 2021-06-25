package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    String videoId;
    int movieId;

    String VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate a binding
        ActivityMovieTrailerBinding binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        // Create a view and set its content
        View view = binding.getRoot();
        setContentView(view);

        // Catch info from Intent
        movieId = getIntent().getIntExtra("id", 0);

        // Resolve the YouTubePlayerView from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // Make an API call to retrieve video ID
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEO_URL, movieId), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d("MovieTrailerActivity", "onSuccess");
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() != 0) {
                        videoId = results.getJSONObject(0).getString("key");
                        // Initialize with API key stored in secrets.xml
                        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                Log.d("MovieTrailerActivity", youTubePlayer.toString());
                                Log.d("MovieTrailerActivity", videoId);
                                youTubePlayer.cueVideo(videoId);
                            }
                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                            }
                        });

                    }

                } catch (JSONException e) {
                    Log.e("MovieTrailerActivity", "Hit JSON exception", e);
                }

            }
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("MovieTrailerActivity", "onFailure");
            }
        });
    }
}