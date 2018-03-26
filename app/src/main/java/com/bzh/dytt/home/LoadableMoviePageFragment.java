package com.bzh.dytt.home;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.bzh.dytt.data.TypeConsts;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;

import java.util.List;

import javax.inject.Inject;

public class LoadableMoviePageFragment extends SingleListFragment<VideoDetail> {

    @Inject
    ViewModelProvider.Factory mFactory;

    public static LoadableMoviePageFragment newInstance(TypeConsts.MovieCategory category) {
        Bundle args = new Bundle();
        args.putInt("MOVIE_CATEGORY", category.ordinal());
        LoadableMoviePageFragment fragment = new LoadableMoviePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        return new MovieListAdapter(this.getContext());
    }

    @Override
    protected void setListData(List<VideoDetail> listData) {
        ((MovieListAdapter) mAdapter).setItems(listData);
    }

    @Override
    protected LiveData<Resource<List<VideoDetail>>> getLiveData() {
        return ((LoadableMoviePageViewModel) mViewModel).getMovieList();
    }

    @Override
    protected ViewModel createViewModel() {
        TypeConsts.MovieCategory category = TypeConsts.MovieCategory.values()[getArguments().getInt("MOVIE_CATEGORY",
                0)];
        LoadableMoviePageViewModel viewModel = ViewModelProviders.of(this, mFactory).get(LoadableMoviePageViewModel
                .class);
        viewModel.setCategory(category);
        return viewModel;
    }

    @Override
    protected void doRefresh() {
        ((LoadableMoviePageViewModel) mViewModel).refresh();

    }
}
