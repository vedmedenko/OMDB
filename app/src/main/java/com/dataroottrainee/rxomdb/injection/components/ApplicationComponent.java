package com.dataroottrainee.rxomdb.injection.components;

import android.app.Application;
import android.content.Context;
import com.dataroottrainee.rxomdb.core.rest.OMDBApi;
import com.dataroottrainee.rxomdb.core.rest.RestModule;
import com.dataroottrainee.rxomdb.core.services.LoadMovieService;
import com.dataroottrainee.rxomdb.injection.ApplicationContext;
import com.dataroottrainee.rxomdb.injection.modules.ApplicationModule;
import com.dataroottrainee.rxomdb.core.services.LoadMoviesService;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {

    void inject(LoadMoviesService service);
    void inject(LoadMovieService service);

    @ApplicationContext
    Context context();

    Application application();

    OMDBApi OMDBApi();
}
