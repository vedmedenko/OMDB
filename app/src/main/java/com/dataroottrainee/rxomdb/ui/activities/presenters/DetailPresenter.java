package com.dataroottrainee.rxomdb.ui.activities.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dataroottrainee.rxomdb.core.DataManager;
import com.dataroottrainee.rxomdb.core.services.LoadMovieService;
import com.dataroottrainee.rxomdb.injection.ApplicationContext;
import com.dataroottrainee.rxomdb.ui.activities.base.BasePresenter;
import com.dataroottrainee.rxomdb.ui.activities.views.DetailMvpView;
import com.dataroottrainee.rxomdb.util.ConstantsManager;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager mDataManager;
    private final Context context;

    private Subscription subscription;
    private BroadcastReceiver br;

    @Inject
    public DetailPresenter(@NonNull @ApplicationContext Context context,
                           @NonNull DataManager dataManager) {
        this.context = context;
        mDataManager = dataManager;
    }

    @Override
    public void attach(DetailMvpView mvpView) {
        super.attach(mvpView);
        createReciever();
    }

    @Override
    public void detach() {
        super.detach();
        if (subscription != null) subscription.unsubscribe();
        deregisterReciever();
    }

    public void loadApiMovie(@NonNull String imdbID) {
        registerReciever();

        Intent intent = new Intent(context, LoadMovieService.class);
        intent.putExtra(ConstantsManager.SE_IMDB_ID, imdbID);
        context.startService(intent);
    }

    private void createReciever() {
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                Bundle extras = intent.getExtras();

                boolean error = extras.getBoolean(ConstantsManager.SE_ERROR, true);

                if (error) {
                    callback();
                } else {
                    String imdbID = extras.getString(ConstantsManager.SE_IMDB_ID);
                    loadDBMovie(imdbID);
                }
            }
        };
    }

    private void registerReciever() {
        IntentFilter intentFilter = new IntentFilter(ConstantsManager.SE_BROADCAST_ACTION_MOVIE);
        context.registerReceiver(br, intentFilter);
    }

    private void deregisterReciever() {
        try {
            context.unregisterReceiver(br);
        } catch (NullPointerException | IllegalArgumentException e) {

        }
    }

    private void callback() {
        getMvpView().showError();
        deregisterReciever();
    }

    public void loadDBMovie(@NonNull String imdbID) {
        subscription = mDataManager.getMovie(imdbID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(movie -> {
                    getMvpView().setupViewPager(movie);
                }, throwable -> {
                    Timber.e("There was an error retrieving the cached movie: " + throwable);
                    getMvpView().showError();
                });
    }
}