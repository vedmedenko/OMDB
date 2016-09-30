package com.dataroottrainee.rxomdb.ui.activities.base;

public interface Presenter<V extends MvpView> {

    void attach(V mvpView);

    void detach();

}
