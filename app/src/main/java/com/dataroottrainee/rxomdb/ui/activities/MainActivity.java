package com.dataroottrainee.rxomdb.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dataroottrainee.rxomdb.R;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.ui.activities.base.BaseActivity;
import com.dataroottrainee.rxomdb.ui.adapters.MovieAdapter;
import com.dataroottrainee.rxomdb.ui.activities.views.MainMvpView;
import com.dataroottrainee.rxomdb.ui.activities.presenters.MainPresenter;
import com.dataroottrainee.rxomdb.util.ConstantsManager;
import com.dataroottrainee.rxomdb.util.DialogFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView {

    @BindView(R.id.progress_indicator) ProgressBar mProgressBar;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_movies) RecyclerView mMoviesRecycler;
    @BindView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.text_view_help) TextView mTextHelp;
    @BindView(R.id.coordinator) CoordinatorLayout mCoordinatorLayout;

    @Inject
    MainPresenter mMainPresenter;

    private Snackbar snack;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupRecyclerView();
        setupItemTouchHelper();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            mTextHelp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        mMainPresenter.attach(this);

        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenter.getMovies();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setupRecyclerView() {
        mMoviesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMovieAdapter = new MovieAdapter(true);
        mMoviesRecycler.setAdapter(mMovieAdapter);

        mSwipeRefresh.setColorSchemeResources(R.color.primary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void setupItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Movie movie = mMovieAdapter.getMovie(viewHolder.getAdapterPosition());

                mMainPresenter.deleteMovie(movie);

                if (snack == null) {
                    snack = Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.snackbar_deleted_movies, mMainPresenter.getDeletedMoviesSize()), Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mMainPresenter.restoreDeletedMovies();
                                    Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.snackbar_restored_movies), Snackbar.LENGTH_SHORT).show();
                                }
                            }).setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    mMainPresenter.clearDeletedMovies();
                                    snack = null;
                                    super.onDismissed(snackbar, event);
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                    super.onShown(snackbar);
                                }
                            });
                } else {
                    snack.setText(getResources().getString(R.string.snackbar_deleted_movies, mMainPresenter.getDeletedMoviesSize()));
                }

                snack.show();
            }
        }).attachToRecyclerView(mMoviesRecycler);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mTextHelp.getVisibility() == View.VISIBLE) {
            savedInstanceState.putBoolean(ConstantsManager.MA_BOOL_TV_HELP_VISIBILITY, true);
        } else {
            savedInstanceState.putBoolean(ConstantsManager.MA_BOOL_TV_HELP_VISIBILITY, false);
        }

        savedInstanceState.putString(ConstantsManager.MA_HELP_TEXT, mTextHelp.getText().toString());

        savedInstanceState.putParcelableArrayList(ConstantsManager.MA_ARRAYLIST_MOVIES, new ArrayList<>(mMovieAdapter.getMovies()));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getBoolean(ConstantsManager.MA_BOOL_TV_HELP_VISIBILITY)) {
            mTextHelp.setVisibility(View.VISIBLE);
        } else {
            mTextHelp.setVisibility(View.GONE);
        }

        mTextHelp.setText(savedInstanceState.getString(ConstantsManager.MA_HELP_TEXT));

        List<Movie> movies = savedInstanceState.getParcelableArrayList(ConstantsManager.MA_ARRAYLIST_MOVIES);
        showMovies(movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(SearchActivity.getStartIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mProgressBar.setVisibility(View.GONE);
        if (movies.size() > 0) {
            mMovieAdapter.setMovies(movies);
            mTextHelp.setVisibility(View.GONE);
        } else {
            showHint(R.string.no_cached_movies);
            mMovieAdapter.setMovies(new ArrayList<Movie>());
        }
    }

    @Override
    public void showError() {
        mTextHelp.setVisibility(View.GONE);
        mMovieAdapter.setMovies(new ArrayList<Movie>());
        DialogFactory.createSimpleOkErrorDialog(this);
    }

    @Override
    public void showHint(@StringRes int res) {
        mTextHelp.setText(getResources().getString(res));
        mTextHelp.setVisibility(View.VISIBLE);
    }
}
