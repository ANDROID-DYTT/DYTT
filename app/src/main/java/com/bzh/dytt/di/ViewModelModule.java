package com.bzh.dytt.di;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.bzh.dytt.home.HomeChildViewModel;
import com.bzh.dytt.home.HomePageViewModel;
import com.bzh.dytt.home.VideoDetailPageViewModel;
import com.bzh.dytt.util.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomePageViewModel.class)
    abstract ViewModel bindHomePageViewModel(HomePageViewModel homePageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeChildViewModel.class)
    abstract ViewModel bindHomeChildViewModel(HomeChildViewModel homeChildViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VideoDetailPageViewModel.class)
    abstract ViewModel bindVideoDetailPageViewModel(VideoDetailPageViewModel videoDetailPageViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
