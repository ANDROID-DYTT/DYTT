package com.bzh.dytt.girl;


import android.util.Log;

public class GirlPresenter implements GirlContract.Presenter {

    private static final String TAG = "GirlPresenter";

    private GirlContract.View mView;

    GirlPresenter(GirlContract.View girlView) {
        mView = girlView;
    }

    @Override
    public void create() {

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

    }
}
