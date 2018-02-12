package com.bzh.dytt.home;

import android.util.Log;

/**
 * Created by biezhihua on 2018/2/12.
 */

class HomePresenter implements HomeContract.Presenter {

    private static final String TAG = "HomePresenter";

    private HomeContract.View mView;

    private HomeViewModel mModel;

    HomePresenter(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void create() {
        Log.d(TAG, "create() called");
        mModel = mView.getViewModel(HomeViewModel.class);

        mModel.getHome().observe(mView.getLifecycleOwner(), mView.getHomeObserver());

    }

    @Override
    public void active() {
        Log.d(TAG, "active() called");
    }

    @Override
    public void inactive() {
        Log.d(TAG, "inactive() called");
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy() called");
    }

    @Override
    public HomeViewModel getHomeViewModel() {
        return mModel;
    }
}
