package com.dataroottrainee.rxomdb.injection.modules;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.dataroottrainee.rxomdb.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    Fragment provideFragment() {
        return mFragment;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mFragment.getContext();
    }

}
