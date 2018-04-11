package com.bzh.dytt;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.bzh.dytt.di.AppInjector;
import com.umeng.commonsdk.UMConfigure;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class BasicApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);

        UMConfigure.init(this, "5acda2b4f29d98253600000c", "Kuan",UMConfigure.DEVICE_TYPE_PHONE , null);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
