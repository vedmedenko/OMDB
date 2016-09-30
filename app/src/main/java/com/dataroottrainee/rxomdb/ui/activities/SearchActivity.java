package com.dataroottrainee.rxomdb.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dataroottrainee.rxomdb.R;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.ui.activities.base.BaseActivity;
import com.dataroottrainee.rxomdb.ui.adapters.MovieAdapter;
import com.dataroottrainee.rxomdb.ui.activities.views.SearchMvpView;
import com.dataroottrainee.rxomdb.ui.activities.presenters.SearchPresenter;
import com.dataroottrainee.rxomdb.util.ConstantsManager;
import com.dataroottrainee.rxomdb.util.DialogFactory;
import com.dataroottrainee.rxomdb.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements SearchMvpView {

    @BindView(R.id.progress_indicator) ProgressBar mProgressBar;
    @BindView(R.id.toolbar)  Toolbar mToolbar;
    @BindView(R.id.et_search) EditText mSearch;
    @BindView(R.id.recycler_movies) RecyclerView mMoviesRecycler;
    @BindView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.text_view_help) TextView mTextHelp;

    @Inject SearchPresenter mSearchPresenter;

    private MovieAdapter mMovieAdapter;

    private final static int pages_to_load_count = 5;
    private int page;
    private boolean load_pages;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setupToolbar();
        setupRecyclerView();
        setupTextChangedListener();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            mTextHelp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        mSearchPresenter.attach(this);
        showHint(R.string.query_too_short);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.detach();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setupRecyclerView() {
        mMoviesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMovieAdapter = new MovieAdapter(false);
        mMoviesRecycler.setAdapter(mMovieAdapter);

        mSwipeRefresh.setColorSchemeResources(R.color.primary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSearch.getText().toString().length() >= 2) {
                    page = 1;
                    // Reloading first page of movies for particular query.
                    askPresenter(mSearch.getText().toString(), page, false);
                } else {
                    showHint(R.string.query_too_short);
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        mMoviesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (load_pages) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if ((lastVisibleItemPosition != RecyclerView.NO_POSITION) && (lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() / 3)) {
                        load_pages = false;
                        int result = page + pages_to_load_count;

                        // Loading <pages_to_load_count> pages of movies to display (with add).

                        for (page++; page <= result; page++) {
                            askPresenter(mSearch.getText().toString(), page, true);
                        }
                    }
                }
            }
        });
    }

    private void setupTextChangedListener() {
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mMovieAdapter.setMovies(new ArrayList<Movie>());
                if (s.toString().length() >= 2) {
                    page = 1;

                    // Initial point from which movies query starts. Asking presenter to load with
                    // replace first page of particular query user wrote to edit text field.

                    askPresenter(s.toString(), page, false);
                } else {
                    showHint(R.string.query_too_short);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(ConstantsManager.MA_STRING_ET_TEXT, mSearch.getText().toString());

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
        mSearch.setText(savedInstanceState.getString(ConstantsManager.MA_STRING_ET_TEXT));

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
    public void setLoadPages(boolean b) {
        load_pages = b;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                finish();
                return true;
            case R.id.action_clear:
                mSearch.setText("");
                showHint(R.string.query_too_short);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);

        if (movies.size() > 0) {
            mTextHelp.setVisibility(View.GONE);
            mMovieAdapter.setMovies(movies);
        } else {
            showHint(R.string.no_movies_found);
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

    @Override
    public void addMovies(List<Movie> movies) {
        mMovieAdapter.addAll(movies);
    }

    private void askPresenter(String query, int page, boolean add) {
        if (NetworkUtil.isNetworkConnected(SearchActivity.this)) {
            mSearchPresenter.loadMovies(query, page, add);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefresh.setRefreshing(false);
            mTextHelp.setVisibility(View.GONE);
            DialogFactory.createSimpleOkErrorDialog(
                    SearchActivity.this,
                    getString(R.string.dialog_error_title),
                    getString(R.string.dialog_error_no_connection)
            ).show();
        }
    }
}
