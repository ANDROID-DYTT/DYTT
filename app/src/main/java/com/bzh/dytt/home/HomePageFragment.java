package com.bzh.dytt.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;

public class HomePageFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {

    private HomeViewModel mHomeViewModel;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    protected HomeContract.Presenter doCreatePresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected void doCreate(@Nullable Bundle savedInstanceState) {
        super.doCreate(savedInstanceState);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

//        mHomeViewModel.getHome().observe(this, new Observer<Home>() {
//            @Override
//            public void onChanged(@Nullable Home home) {
//
//            }
//        });
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page, container, false);
    }

    @Override
    protected void doResume() {
        super.doResume();
    }

}
