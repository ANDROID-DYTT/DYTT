package com.bzh.dytt;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.bzh.dytt.home.VideoDetailPageFragment;
import com.bzh.dytt.search.SearchFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class SingleActivity extends BaseActivity implements HasSupportFragmentInjector {

    public static final String TYPE = "TYPE";
    public static final String DATA = "DATA";
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    public static void startDetailPage(Activity activity, String detailLink) {
        Intent intent = new Intent(activity, SingleActivity.class);
        intent.putExtra(TYPE, SingleType.Detail.getValue());
        intent.putExtra(DATA, detailLink);
        activity.startActivity(intent);
    }

    public static void startSearchPage(Activity activity) {
        Intent intent = new Intent(activity, SingleActivity.class);
        intent.putExtra(TYPE, SingleType.Search.getValue());
        activity.startActivity(intent);
    }

    @Override
    protected void doCreate() {
        setContentView(R.layout.activity_single);
        setupActionBar();

        Intent intent = getIntent();

        BaseFragment fragment = null;

        int type = intent.getIntExtra(TYPE, SingleType.Detail.getValue());

        if (SingleType.Detail.getValue() == type) {
            String detailLink = intent.getStringExtra(DATA);
            assert detailLink != null;
            fragment = VideoDetailPageFragment.newInstance(detailLink);
        }

        if (SingleType.Search.getValue() == type) {
            fragment = SearchFragment.newInstance();
        }

        assert fragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private enum SingleType {
        Detail(1),
        Search(2);

        private int mValue;

        SingleType(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

}
