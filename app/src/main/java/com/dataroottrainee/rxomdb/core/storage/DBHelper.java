package com.dataroottrainee.rxomdb.core.storage;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DBHelper {

    private final BriteDatabase mBriteDatabase;

    @Inject
    public DBHelper(@NonNull DBOpenHelper dbOpenHelper) {
        mBriteDatabase = SqlBrite.create(message -> {
            Timber.tag("Database").v(message);
        }).wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public Observable<List<Movie>> selectMovies() {
        return mBriteDatabase.createQuery(MovieModel.TABLE_NAME, MovieModel.SELECT_ALL).mapToList(Movie.SELECT_ALL_MAPPER::map);
    }

    public Observable<Movie> selectMovie(@NonNull String imdbID) {
        return mBriteDatabase.createQuery(MovieModel.TABLE_NAME, MovieModel.SELECT_BY_ID, imdbID).mapToOne(Movie.SELECT_BY_ID_MAPPER::map);
    }

    public Observable<Movie> insertMovie(@NonNull Movie movie) {
        return Observable.defer(() -> {
            BriteDatabase.Transaction transaction = mBriteDatabase.newTransaction();

            try {
                long result = mBriteDatabase.insert(MovieModel.TABLE_NAME,
                        Movie.FACTORY.marshal(movie).asContentValues(),
                        SQLiteDatabase.CONFLICT_REPLACE);

                if (result < 0) Timber.e("Failed to insert data: " + result);
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }

            return Observable.just(movie);
        });
    }

    public void insertMovies(@NonNull List<Movie> movies) {
        for (Movie movie : movies) {
            insertMovie(movie).subscribe(mov -> {

            });
        }
    }

    public Observable<Movie> deleteMovie(@NonNull String imdbID) {
        return Observable.defer(() -> {
            Observable<Movie> movieObservable = selectMovie(imdbID);

            BriteDatabase.Transaction transaction = mBriteDatabase.newTransaction();

            try {
                long result = mBriteDatabase.delete(MovieModel.TABLE_NAME,
                        MovieModel.IMDBID + "= ?",
                        imdbID);

                if (result < 0) Timber.e("Failed to delete data: " + result);
                transaction.markSuccessful();
            } finally {
                transaction.end();
            }

            return movieObservable;
        });

    }
}
