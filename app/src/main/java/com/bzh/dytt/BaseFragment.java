package com.bzh.dytt;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
