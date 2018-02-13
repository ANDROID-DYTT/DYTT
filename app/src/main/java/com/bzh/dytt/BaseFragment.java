package com.bzh.dytt;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.dytt.exception.PresenterException;

import butterknife.ButterKnife;

public abstract class BaseFragment<T extends IPresenter> extends Fragment implements IView<T> {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mPresenter = doCreatePresenter();

        if (mPresenter == null) {
            throw new PresenterException("Presenter is null, did you forget to call createPresenter?");
        }

        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(mPresenter);

        doCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = doCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public final void onResume() {
        super.onResume();
        doResume();
    }

    @Override
    public final void onPause() {
        doPause();
        super.onPause();
    }

    protected abstract T doCreatePresenter();

    protected void doCreate(@Nullable Bundle savedInstanceState) {

    }

    protected abstract View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected void doResume() {
        // Override this method in derived fragment
        // Do not do anything here
    }

    protected void doPause() {
        // Override this method in derived fragment
        // Do not do anything here
    }
}
