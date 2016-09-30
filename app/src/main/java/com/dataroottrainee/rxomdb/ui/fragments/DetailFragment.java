package com.dataroottrainee.rxomdb.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dataroottrainee.rxomdb.R;
import com.dataroottrainee.rxomdb.ui.adapters.DetailAdapter;
import com.dataroottrainee.rxomdb.util.ConstantsManager;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends BaseFragment {

    @BindView(R.id.recycler_detail) RecyclerView mDetailRecycler;
    @BindView(R.id.text_no_data) TextView mNoDataText;
    @BindView(R.id.image_movie) ImageView mImageMovie;

    @Inject
    DetailAdapter mDetailAdapter;

    private LinkedHashMap<String, String> mItems;
    private String mPoster;

    public static DetailFragment newInstance(LinkedHashMap<String, String> items, String poster) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ConstantsManager.DF_ARG_MAP, items);
        args.putString(ConstantsManager.DF_ARG_POSTER, poster);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        mItems = (LinkedHashMap<String, String>) getArguments().getSerializable(ConstantsManager.DF_ARG_MAP);
        mPoster = getArguments().getString(ConstantsManager.DF_ARG_POSTER);
        if (mItems == null) {
            throw new IllegalArgumentException("DetailFragment requires a hashmap of details!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, fragmentView);
        if (!mPoster.isEmpty()) {
            Glide.with(getActivity())
                    .load(mPoster)
                    .into(mImageMovie);
            mImageMovie.setVisibility(View.VISIBLE);
        }
        setupRecyclerView();
        return fragmentView;
    }

    private void setupRecyclerView() {
        mDetailRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mItems != null && mItems.size() > 0) {
            mDetailRecycler.setAdapter(mDetailAdapter);
            mDetailAdapter.setDetails(mItems);
            mDetailRecycler.setVisibility(View.VISIBLE);
        } else {
            mNoDataText.setVisibility(View.VISIBLE);
        }
    }

}
