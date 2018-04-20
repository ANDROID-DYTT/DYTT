package com.bzh.dytt.di;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.bzh.dytt.BaseViewModel;
import com.bzh.dytt.viewmodel.ImdbViewModel;
import com.bzh.dytt.viewmodel.LoadableMoviePageViewModel;
import com.bzh.dytt.viewmodel.NewMovieViewModel;
import com.bzh.dytt.viewmodel.VideoDetailPageViewModel;
import com.bzh.dytt.search.SearchViewModel;
import com.bzh.dytt.util.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(VideoDetailPageViewModel.class)
    abstract ViewModel bindVideoDetailPageViewModel(VideoDetailPageViewModel videoDetailPageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewMovieViewModel.class)
    abstract ViewModel bindNewMoviePageViewModel(NewMovieViewModel newMovieViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BaseViewModel.class)
    abstract ViewModel bindBaseViewModel(BaseViewModel baseViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoadableMoviePageViewModel.class)
    abstract ViewModel bindLoadableMoviePageViewModel(LoadableMoviePageViewModel loadableMoviePageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImdbViewModel.class)
    abstract ViewModel bindImdbViewModel(ImdbViewModel imdbViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
