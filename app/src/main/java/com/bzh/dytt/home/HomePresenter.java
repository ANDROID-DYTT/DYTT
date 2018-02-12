package com.bzh.dytt.home;

import android.util.Log;

/**
 * Created by biezhihua on 2018/2/12.
 */

class HomePresenter implements HomeContract.Presenter {

    private static final String TAG = "HomePresenter";

    private HomeContract.View mView;

    HomePresenter(HomeContract.View view) {

        mView = view;
    }

    @Override
    public void subscribe() {
        Log.d(TAG, "subscribe() called");
    }

    @Override
    public void unSubscribe() {
        Log.d(TAG, "unSubscribe() called");
    }
}
