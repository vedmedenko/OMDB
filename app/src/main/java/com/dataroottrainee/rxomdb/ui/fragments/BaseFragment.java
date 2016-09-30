package com.dataroottrainee.rxomdb.ui.fragments;

import android.support.v4.app.Fragment;

import com.dataroottrainee.rxomdb.injection.components.DaggerFragmentComponent;
import com.dataroottrainee.rxomdb.injection.components.FragmentComponent;
import com.dataroottrainee.rxomdb.injection.modules.FragmentModule;

public class BaseFragment extends Fragment {

    private FragmentComponent mFragmentComponent;

    protected FragmentComponent fragmentComponent() {
        if (mFragmentComponent == null) {
            mFragmentComponent = DaggerFragmentComponent.builder()
                    .fragmentModule(new FragmentModule(this))
                    .build();
        }
        return mFragmentComponent;
    }

}
