package com.dataroottrainee.rxomdb.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import com.dataroottrainee.rxomdb.R;
import com.dataroottrainee.rxomdb.core.storage.entities.Movie;
import com.dataroottrainee.rxomdb.ui.activities.base.BaseActivity;
import com.dataroottrainee.rxomdb.ui.fragments.DetailFragment;
import com.dataroottrainee.rxomdb.ui.activities.views.DetailMvpView;
import com.dataroottrainee.rxomdb.ui.activities.presenters.DetailPresenter;
import com.dataroottrainee.rxomdb.util.ConstantsManager;
import com.dataroottrainee.rxomdb.util.DialogFactory;
import com.dataroottrainee.rxomdb.util.NetworkUtil;
import java.util.LinkedHashMap;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements DetailMvpView {

    @BindView(R.id.sliding_tabs) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.pager_movie_detail) ViewPager mMovieDetailPager;
    @BindView(R.id.progress_indicator) ProgressBar mProgressBar;

    @Inject DetailPresenter mDetailPresenter;

    public static Intent getStartIntent(Context context, String imdbID, boolean cached) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ConstantsManager.DA_EXTRA_MOVIE, imdbID);
        intent.putExtra(ConstantsManager.DA_EXTRA_CACHED, cached);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        activityComponent().inject(this);
        ButterKnife.bind(this);

        mDetailPresenter.attach(this);

        String imdbID = getIntent().getStringExtra(ConstantsManager.DA_EXTRA_MOVIE);
        boolean cached = getIntent().getBooleanExtra(ConstantsManager.DA_EXTRA_CACHED, false);

        if (imdbID.isEmpty()) {
            throw new IllegalArgumentException("DetailActivity requires a Movie object (imdbID must be passed)!");
        }

        setupToolbar();

        if (!cached) {
            if (NetworkUtil.isNetworkConnected(this)) {
                mDetailPresenter.loadApiMovie(imdbID);
            } else {
                mProgressBar.setVisibility(View.GONE);
                DialogFactory.createSimpleOkErrorDialog(
                        this,
                        getString(R.string.dialog_error_title),
                        getString(R.string.dialog_error_no_connection)
                ).show();
            }
        } else {
            mDetailPresenter.loadDBMovie(imdbID);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.label_detail));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.detach();
    }

    @Override
    public void setupViewPager(final Movie movie) {
        mProgressBar.setVisibility(View.GONE);

        mMovieDetailPager.setOffscreenPageLimit(2);
        mMovieDetailPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            String[] titles = getResources().getStringArray(R.array.detail_fragment_titles);

            @Override
            public Fragment getItem(int position) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                switch (position) {
                    case 0:
                        map.put("Title: ", movie.title());
                        map.put("Year: ", movie.year());
                        map.put("Type: ", movie.type());
                        return DetailFragment.newInstance(map, movie.poster());
                    case 1:
                        map.put("Rated: ", movie.rated());
                        map.put("Released: ", movie.released());
                        map.put("Runtime: ", movie.runtime());
                        map.put("Genre: ", movie.genre());
                        map.put("Language: ", movie.lang());
                        return DetailFragment.newInstance(map, "");
                    case 2:
                        map.put("Director: ", movie.director());
                        map.put("Writer: ", movie.writer());
                        map.put("Actors: ", movie.actors());
                        map.put("Plot: ", movie.plot());
                        map.put("Country: ", movie.country());
                        return DetailFragment.newInstance(map, "");
                    case 3:
                        map.put("Awards: ", movie.awards());
                        map.put("Metascore: ", movie.metascore());
                        map.put("imdbVotes: ", movie.imdbVotes());
                        map.put("imdbRating: ", movie.imdbRating());
                        map.put("imdbID: ", movie.imdbID());
                        return DetailFragment.newInstance(map, "");
                    default:
                        return DetailFragment.newInstance(map, "");
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        });

        mTabLayout.setupWithViewPager(mMovieDetailPager);
        mMovieDetailPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        mProgressBar.setVisibility(View.GONE);
        DialogFactory.createSimpleOkErrorDialog(this);
    }
}
