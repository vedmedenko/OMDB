package com.dataroottrainee.rxomdb.ui.activities.presenters;

import android.support.annotation.NonNull;

import com.dataroottrainee.rxomdb.core.DataManager;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.ui.activities.base.BasePresenter;
import com.dataroottrainee.rxomdb.ui.activities.views.MainMvpView;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Subscription subscription;
    private ArrayList<Movie> mDeletedMovies;

    @Inject
    public MainPresenter(@NonNull DataManager dataManager) {
        mDataManager = dataManager;
        mDeletedMovies = new ArrayList<>();
    }

    @Override
    public void attach(MainMvpView mvpView) {
        super.attach(mvpView);
    }

    @Override
    public void detach() {
        super.detach();
        if (subscription != null) subscription.unsubscribe();
    }

    public void getMovies() {
        subscription = mDataManager.getMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(movies -> {
                    getMvpView().showMovies(movies);
                }, throwable -> {
                    Timber.e("There was an error retrieving cached movies: " + throwable);
                    getMvpView().showError();
                });
    }

    public void deleteMovie(Movie movie) {
        mDeletedMovies.add(movie);
        mDataManager.deleteMovie(movie.imdbID()).subscribe(mov -> {

        });
    }

    public void clearDeletedMovies() {
        mDeletedMovies.clear();
    }

    public void restoreDeletedMovies() {
        Timber.d("mDeletedMovies size = " + mDeletedMovies.size());
        mDataManager.insertMovies(mDeletedMovies);
        clearDeletedMovies();
    }

    public int getDeletedMoviesSize() {
        return mDeletedMovies.size();
    }
}
