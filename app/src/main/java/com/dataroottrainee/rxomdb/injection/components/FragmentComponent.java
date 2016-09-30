package com.dataroottrainee.rxomdb.injection.components;

import com.dataroottrainee.rxomdb.injection.modules.FragmentModule;
import com.dataroottrainee.rxomdb.injection.PerFragment;
import com.dataroottrainee.rxomdb.ui.fragments.DetailFragment;

import dagger.Component;

@PerFragment
@Component(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(DetailFragment detailFragment);

}
