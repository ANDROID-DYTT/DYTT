package com.bzh.dytt.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bzh.dytt.SingleListFragment;
import com.bzh.dytt.data.ExceptionType;
import com.bzh.dytt.data.Resource;
import com.bzh.dytt.data.entity.VideoDetail;
import com.bzh.dytt.viewmodel.NewMovieViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

public class NewMovieFragment extends SingleListFragment<VideoDetail> {

    @Inject
    ViewModelProvider.Factory mFactory;

    public static NewMovieFragment newInstance() {
        return new NewMovieFragment();
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        return new MovieListAdapter(this.getContext(), ((NewMovieViewModel) mViewModel).getVideoDetailLiveData());
    }

    @Override
    protected void replace(@NotNull List<? extends VideoDetail> listData) {
        ((MovieListAdapter) mAdapter).replace(listData);
    }

    @Override
    protected LiveData<Resource<List<VideoDetail>>> getListLiveData() {
        return ((NewMovieViewModel) mViewModel).getNewMovieList();
    }

    @Override
    protected ViewModel createViewModel() {
        return ViewModelProviders.of(this, mFactory).get(NewMovieViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLifecycle().addObserver(((NewMovieViewModel) mViewModel));
    }

    @Override
    protected void doDestroyView() {
        getLifecycle().removeObserver(((NewMovieViewModel) mViewModel));
        super.doDestroyView();
    }

    @Override
    protected void doRefresh() {
        ((NewMovieViewModel) mViewModel).refresh();
    }

    @Override
    protected LiveData<Resource<ExceptionType>> getThrowableLiveData() {
        return ((NewMovieViewModel) mViewModel).getFetchVideoDetailState();
    }
}
