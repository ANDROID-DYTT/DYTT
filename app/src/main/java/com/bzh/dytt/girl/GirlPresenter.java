package com.bzh.dytt.girl;


public class GirlPresenter implements GirlContract.Presenter {

    private GirlContract.View mGirlView;

    GirlPresenter(GirlContract.View girlView) {
        mGirlView = girlView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

}