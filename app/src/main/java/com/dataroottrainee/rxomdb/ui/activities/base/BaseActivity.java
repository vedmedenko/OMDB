package com.dataroottrainee.rxomdb.ui.activities.base;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dataroottrainee.rxomdb.RxOMDBApplication;
import com.dataroottrainee.rxomdb.injection.components.ActivityComponent;
import com.dataroottrainee.rxomdb.injection.components.DaggerActivityComponent;
import com.dataroottrainee.rxomdb.injection.modules.ActivityModule;

import java.lang.ref.WeakReference;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;
    private WeakReference<Context> weakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weakReference = new WeakReference<>(this);
    }

    protected ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(RxOMDBApplication.get(weakReference).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        weakReference.clear();
        super.onDestroy();
        watchForLeaks();
    }

    private void watchForLeaks() {
        RxOMDBApplication.getRefWatcher(this).watch(this);
    }
}