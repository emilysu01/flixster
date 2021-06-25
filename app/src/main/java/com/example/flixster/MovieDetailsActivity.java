package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivPosterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate a binding
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        // Create a view and set its content
        View view = binding.getRoot();
        setContentView(view);

        // Retrieve, unwrap, and assign movie
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        // Add some logging to confirm deserialization
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // Retrieve UI components
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivPosterImage = (ImageView) findViewById(R.id.ivPrimaryImage);

        // Set UI components
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        float voteAvg = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAvg = voteAvg > 0 ? voteAvg / 2.0f : voteAvg);
        Drawable progress = rbVoteAverage.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.parseColor("#FFE45E"));

        // Display images using Glide (different images are displayed if the phone is in landscape vs. portrait)
        int radius = 30;
        int margin = 10;
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Glide.with(getApplicationContext())
                    .load(movie.getBackdropPath())
                    .centerInside()
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPosterImage);
        } else {
            Glide.with(getApplicationContext())
                    .load(movie.getPosterPath())
                    .centerInside()
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPosterImage);
        }

        // Add functionality to poster
        ivPosterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MovieTrailerActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }
        });
    }

}