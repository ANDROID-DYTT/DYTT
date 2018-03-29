package com.bzh.dytt.di;

import com.bzh.dytt.colorhunt.ColorHuntFragment;
import com.bzh.dytt.girl.GirlFragment;
import com.bzh.dytt.home.AllMoviePageFragment;
import com.bzh.dytt.home.LoadableMoviePageFragment;
import com.bzh.dytt.home.NewMovieFragment;
import com.bzh.dytt.home.VideoDetailPageFragment;
import com.bzh.dytt.search.SearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract AllMoviePageFragment contributeHomePageFragment();

    @ContributesAndroidInjector
    abstract ColorHuntFragment contributeColorHuntFragment();

    @ContributesAndroidInjector
    abstract GirlFragment contributeGirlFragment();

    @ContributesAndroidInjector
    abstract VideoDetailPageFragment contributeVideoDetailFragment();

    @ContributesAndroidInjector
    abstract NewMovieFragment contributeNewMovieFragment();

    @ContributesAndroidInjector
    abstract LoadableMoviePageFragment contributeLoadbleMoviePageFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
