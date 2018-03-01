package com.bzh.dytt.di;

import com.bzh.dytt.colorhunt.ColorHuntFragment;
import com.bzh.dytt.girl.GirlFragment;
import com.bzh.dytt.home.HomeChildFragment;
import com.bzh.dytt.home.HomePageFragment;
import com.bzh.dytt.home.VideoDetailPageFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract HomePageFragment contributeHomePageFragment();

    @ContributesAndroidInjector
    abstract ColorHuntFragment contributeColorHuntFragment();

    @ContributesAndroidInjector
    abstract GirlFragment contributeGirlFragment();

    @ContributesAndroidInjector
    abstract HomeChildFragment contributeHomeChildFragment();

    @ContributesAndroidInjector
    abstract VideoDetailPageFragment contributeVideoDetailFragment();
}
