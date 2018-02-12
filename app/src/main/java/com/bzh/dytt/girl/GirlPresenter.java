package com.bzh.dytt.girl;


import android.util.Log;

public class GirlPresenter implements GirlContract.Presenter {

    private static final String TAG = "GirlPresenter";

    private GirlContract.View mView;

    GirlPresenter(GirlContract.View girlView) {
        mView = girlView;
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
