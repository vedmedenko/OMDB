package com.dataroottrainee.rxomdb;

import android.app.Application;
import android.content.Context;

import com.dataroottrainee.rxomdb.injection.components.ApplicationComponent;
import com.dataroottrainee.rxomdb.injection.components.DaggerApplicationComponent;
import com.dataroottrainee.rxomdb.injection.modules.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class RxOMDBApplication extends Application {

    private ApplicationComponent applicationComponent;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        initTimber();
        initLeakCanary();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        return applicationComponent;
    }

    public static RefWatcher getRefWatcher(Context context) {
        return ((RxOMDBApplication) context.getApplicationContext()).refWatcher;
    }

    public static RxOMDBApplication get(WeakReference<Context> contextWeakReference) {
        return (RxOMDBApplication) contextWeakReference.get().getApplicationContext();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initLeakCanary() {
        refWatcher = LeakCanary.install(this);
    }
}
