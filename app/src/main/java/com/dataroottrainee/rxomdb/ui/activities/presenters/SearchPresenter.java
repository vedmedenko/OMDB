package com.dataroottrainee.rxomdb.ui.activities.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dataroottrainee.rxomdb.R;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.core.services.LoadMoviesService;
import com.dataroottrainee.rxomdb.injection.ApplicationContext;
import com.dataroottrainee.rxomdb.ui.activities.base.BasePresenter;
import com.dataroottrainee.rxomdb.ui.activities.views.SearchMvpView;
import com.dataroottrainee.rxomdb.util.ConstantsManager;

import java.util.ArrayList;
import javax.inject.Inject;

public class SearchPresenter extends BasePresenter<SearchMvpView> {

    private Context context;
    private BroadcastReceiver br;

    @Inject
    public SearchPresenter(@NonNull @ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void attach(SearchMvpView mvpView) {
        super.attach(mvpView);
        createReciever();
    }

    @Override
        public void detach() {
        super.detach();
        deregisterReciever();
    }

    public void loadMovies(String query, int page, boolean add) {
        registerReciever();

        Intent intent = new Intent(context, LoadMoviesService.class);
        intent.putExtra(ConstantsManager.SE_QUERY, query);
        intent.putExtra(ConstantsManager.SE_PAGE, page);
        intent.putExtra(ConstantsManager.SE_ADD, add);
        context.startService(intent);
    }

    public void callback(ArrayList<Movie> movies, boolean ok, boolean add) {
        getMvpView().setLoadPages(ok);
        if (!add) {
            if (movies.size() == 0) {
                getMvpView().showMovies(new ArrayList<Movie>());
                getMvpView().showHint(R.string.no_movies_found);
            } else {
                getMvpView().showMovies(movies);
            }
        } else {
            getMvpView().addMovies(movies);
        }
        deregisterReciever();
    }

    public void callback() {
        getMvpView().showError();
        deregisterReciever();
    }

    private void createReciever() {
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                Bundle extras = intent.getExtras();

                boolean error = extras.getBoolean(ConstantsManager.SE_ERROR, true);

                if (error) {
                    callback();
                } else {
                    ArrayList<Movie> movies = extras.getParcelableArrayList(ConstantsManager.SE_ARRAYLIST_MOVIES);
                    boolean ok = extras.getBoolean(ConstantsManager.SE_OK);
                    boolean add = extras.getBoolean(ConstantsManager.SE_ADD);
                    callback(movies, ok, add);
                }
            }
        };
    }

    private void registerReciever() {
        IntentFilter intentFilter = new IntentFilter(ConstantsManager.SE_BROADCAST_ACTION_MOVIES);
        context.registerReceiver(br, intentFilter);
    }

    private void deregisterReciever() {
        try {
            context.unregisterReceiver(br);
        } catch (IllegalArgumentException | NullPointerException e) {

        }

    }
}