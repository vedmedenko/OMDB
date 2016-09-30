package com.dataroottrainee.rxomdb.injection.components;

import com.dataroottrainee.rxomdb.injection.PerActivity;
import com.dataroottrainee.rxomdb.injection.modules.ActivityModule;
import com.dataroottrainee.rxomdb.ui.activities.DetailActivity;
import com.dataroottrainee.rxomdb.ui.activities.MainActivity;
import com.dataroottrainee.rxomdb.ui.activities.SearchActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
    void inject(SearchActivity searchActivity);
}
