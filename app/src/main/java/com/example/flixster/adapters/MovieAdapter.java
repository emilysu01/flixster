package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

// MovieAdapter extends the base RecyclerView.Adapter and is parameterized by a ViewHolder
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        // Inflate the layout
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // Get the movie at position
        Movie movie = movies.get(position);
        // Bind the movie data into the ViewHolder
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // ViewHolder is a row in the RecyclerView (inlcudes movie poster, title, and overview)
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            // Retrieving components from UI
            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);

            // Set onClickListener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get position and ensure it's valid
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Get the Movie at position in the list
                Movie movie = movies.get(position);
                // Create an Intent to display MovieDetailsActivity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // Pass the movie as an extra serialized via Parcels.wrap()
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Show the activity
                context.startActivity(intent);
            }
        }

        public void bind(Movie movie) {
            // Set text for TextViews
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // Display images using Glide (different images are displayed if the phone is in landscape vs. portrait)
            int radius = 30;
            int margin = 10;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Glide.with(context)
                        .load(movie.getBackdropPath())
                        .centerInside()
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(ivPoster);
            } else {
                Glide.with(context)
                        .load(movie.getPosterPath())
                        .centerInside()
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(ivPoster);
            }
        }
    }

}
