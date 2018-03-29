package com.bzh.dytt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bzh.dytt.home.VideoDetailPageFragment;
import com.bzh.dytt.search.SearchFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class SingleActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static final String TYPE = "TYPE";
    public static final String DATA = "DATA";

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

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

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

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

}
