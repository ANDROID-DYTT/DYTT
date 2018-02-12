package com.bzh.dytt;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

public interface IPresenter extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void create();

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void active();

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void inactive();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy();
}
