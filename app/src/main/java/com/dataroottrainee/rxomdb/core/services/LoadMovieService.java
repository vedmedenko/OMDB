package com.dataroottrainee.rxomdb.core.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dataroottrainee.rxomdb.RxOMDBApplication;
import com.dataroottrainee.rxomdb.core.DataManager;
import com.dataroottrainee.rxomdb.util.ConstantsManager;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoadMovieService extends Service {

    @Inject DataManager mDataManager;

    private Subscription subscription;
    private WeakReference<Context> contextWeakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        contextWeakReference = new WeakReference<>(this);
        RxOMDBApplication.get(contextWeakReference).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();

        String imdbID = extras.getString(ConstantsManager.SE_IMDB_ID, "");

        if (!imdbID.isEmpty()) {

            subscription = mDataManager.loadMovie(imdbID)
                    .subscribeOn(Schedulers.io())
                    .doAfterTerminate(() -> {
                        stopSelf(startId);
                    }).subscribe(movie -> {
                        Intent broadcast = new Intent(ConstantsManager.SE_BROADCAST_ACTION_MOVIE);
                        broadcast.putExtra(ConstantsManager.SE_IMDB_ID, imdbID);
                        broadcast.putExtra(ConstantsManager.SE_ERROR, false);
                        sendBroadcast(broadcast);
                    }, throwable -> {
                        Intent broadcast = new Intent(ConstantsManager.SE_BROADCAST_ACTION_MOVIE);
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
