package com.dataroottrainee.rxomdb.core;

import android.support.annotation.NonNull;

import com.dataroottrainee.rxomdb.core.storage.DBHelper;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.core.rest.models.MoviesResponse;
import com.dataroottrainee.rxomdb.core.rest.OMDBApi;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import rx.Observable;

public class DataManager {

    private final OMDBApi mOMDBService;
    private final DBHelper mDBManager;

    @Inject
    public DataManager(@NonNull OMDBApi service, @NonNull DBHelper manager) {
        mOMDBService = service;
        mDBManager = manager;
    }

    @NonNull
    public Observable<MoviesResponse> loadMovies(@NonNull String query, int page) {
        return mOMDBService.getMovies(query, page);
    }

    @NonNull
    public Observable<Movie> loadMovie(@NonNull String imdbID) {
        return mOMDBService.getMovie(imdbID).concatMap(movie -> insertMovie(movie.toDBType()));
    }

    @NonNull
    public Observable<List<Movie>> getMovies() {
        return mDBManager.selectMovies();
    }

    @NonNull
    public Observable<Movie> getMovie(@NonNull String imdbID) {
        return mDBManager.selectMovie(imdbID);
    }

    @NonNull
    public Observable<Movie> insertMovie(@NonNull Movie movie) {
        return mDBManager.insertMovie(movie);
    }

    @NonNull
    public void insertMovies(@NonNull ArrayList<Movie> movies) {
        mDBManager.insertMovies(movies);
    }

    @NonNull
    public Observable<Movie> deleteMovie(@NonNull String imdbID) {
        return mDBManager.deleteMovie(imdbID);
    }
}
