package com.dataroottrainee.rxomdb.core.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dataroottrainee.rxomdb.RxOMDBApplication;
import com.dataroottrainee.rxomdb.core.DataManager;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.util.ConstantsManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;

public class LoadMoviesService extends Service {

    @Inject DataManager mDataManager;

    private Subscription subscription;
    private WeakReference<Context> contextWeakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        contextWeakReference = new WeakReference<Context>(this);
        RxOMDBApplication.get(contextWeakReference).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();

        String query = extras.getString(ConstantsManager.SE_QUERY, "");
        int page = extras.getInt(ConstantsManager.SE_PAGE, -1);
        boolean add = extras.getBoolean(ConstantsManager.SE_ADD);

        if (!query.isEmpty() && page > -1) {

            subscription = mDataManager.loadMovies(query, page)
                    .subscribeOn(Schedulers.io())
                    .doAfterTerminate(() -> {
                        stopSelf(startId);
                    }).subscribe((moviesResponse) -> {
                        ArrayList<Movie> movies = moviesResponse.getMovies();
                        Intent broadcast = new Intent(ConstantsManager.SE_BROADCAST_ACTION_MOVIES);
                        broadcast.putExtra(ConstantsManager.SE_OK, moviesResponse.getOK());
                        broadcast.putParcelableArrayListExtra(ConstantsManager.SE_ARRAYLIST_MOVIES, movies);
                        broadcast.putExtra(ConstantsManager.SE_ERROR, false);
                        broadcast.putExtra(ConstantsManager.SE_ADD, add);
                        sendBroadcast(broadcast);
                    }, throwable -> {
                        Intent broadcast = new Intent(ConstantsManager.SE_BROADCAST_ACTION_MOVIES);
                        broadcast.putExtra(ConstantsManager.SE_ERROR, true);
                        sendBroadcast(broadcast);
                    });
        } else {
            stopSelf(startId);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contextWeakReference.clear();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
