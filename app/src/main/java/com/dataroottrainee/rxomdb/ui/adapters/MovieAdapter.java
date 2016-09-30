package com.dataroottrainee.rxomdb.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dataroottrainee.rxomdb.R;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.ui.activities.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> mMovies;

    private boolean main;

    public MovieAdapter(boolean main) {
        mMovies = new ArrayList<>();
        this.main = main;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_card, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        final Movie movie = mMovies.get(position);

        holder.titleText.setText(movie.title());

        try {
            holder.typeText.setText(context.getString(R.string.movie_type_to_uppercase, movie.type().substring(0, 1).toUpperCase(), movie.type().substring(1)));
        } catch (NullPointerException e) {
            holder.typeText.setText("");
        }

        holder.yearText.setText(movie.year());
        Glide.with(context)
                .load(movie.poster())
                .into(holder.movieImage);


        holder.movieContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(DetailActivity.getStartIntent(context, movie.imdbID(), main));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public Movie getMovie(int location) {
        return mMovies.get(location);
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_container)
        View movieContainer;

        @BindView(R.id.text_title)
        TextView titleText;

        @BindView(R.id.text_type)
        TextView typeText;

        @BindView(R.id.text_year)
        TextView yearText;

        @BindView(R.id.image_movie)
        ImageView movieImage;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
