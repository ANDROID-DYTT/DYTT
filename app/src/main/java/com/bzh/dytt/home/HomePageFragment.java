package com.bzh.dytt.home;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;

public class HomePageFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    protected HomeContract.Presenter doCreatePresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page, container, false);
    }

    @Override
    protected void doResume() {
        super.doResume();
    }

    @Override
    public Observer<Home> getHomeObserver() {
        return new Observer<Home>() {
            @Override
            public void onChanged(@Nullable Home home) {

            }
        };
    }
}
