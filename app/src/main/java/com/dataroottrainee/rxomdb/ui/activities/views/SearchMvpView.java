package com.dataroottrainee.rxomdb.ui.activities.views;

import android.support.annotation.StringRes;

import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.ui.activities.base.MvpView;

import java.util.List;

public interface SearchMvpView extends MvpView {
    void showMovies(List<Movie> movies);
    void showError();
    void showHint(@StringRes int res);
    void setLoadPages(boolean b);
    void addMovies(List<Movie> movies);
}
