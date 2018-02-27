package com.bzh.dytt.di;

import com.bzh.dytt.MainActivity;
import com.bzh.dytt.SingleActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract SingleActivity contributeSingleActivity();
}

