package com.bzh.dytt.home;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;

import com.bzh.dytt.IPresenter;
import com.bzh.dytt.IView;

public interface HomeContract {

    interface View extends IView<Presenter> {

    }

    interface Presenter extends IPresenter {
    }
}
