package com.dataroottrainee.rxomdb.ui.activities.views;

import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.ui.activities.base.MvpView;

public interface DetailMvpView extends MvpView {
    void setupViewPager(final Movie movie);
    void showError();
}
