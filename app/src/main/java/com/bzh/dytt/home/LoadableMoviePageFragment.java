package com.bzh.dytt.home;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.DataTypeConverter;
import com.bzh.dytt.data.network.Resource;

import java.util.List;

import javax.inject.Inject;

public class LoadableMoviePageFragment extends SingleListFragment<VideoDetail> {

    @Inject
    ViewModelProvider.Factory mFactory;

    public static LoadableMoviePageFragment newInstance(MovieCategory category) {
        Bundle args = new Bundle();
        args.putInt("MOVIE_CATEGORY", category.getId());
        LoadableMoviePageFragment fragment = new LoadableMoviePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        return new MovieListAdapter(this.getContext());
    }

    @Override
    protected void replace(List<VideoDetail> listData) {
        ((MovieListAdapter) mAdapter).replace(listData);
    }

    @Override
    protected LiveData<Resource<List<VideoDetail>>> getListLiveData() {
        return ((LoadableMoviePageViewModel) mViewModel).getMovieList();
    }

    @Override
    protected ViewModel createViewModel() {
        MovieCategory category = DataTypeConverter.intToEnum(getArguments().getInt("MOVIE_CATEGORY", 0));
        LoadableMoviePageViewModel viewModel = ViewModelProviders.of(this, mFactory).get(LoadableMoviePageViewModel
                .class);
        viewModel.setCategory(category);
        return viewModel;
    }

    @Override
    protected void doViewCreated(View view, Bundle savedInstanceState) {
        super.doViewCreated(view, savedInstanceState);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if (lastPosition == mAdapter.getItemCount() - 1) {
                    ((LoadableMoviePageViewModel) mViewModel).loadNextPage();
                }
            }
        });
    }

    @Override
    protected void doRefresh() {
        ((LoadableMoviePageViewModel) mViewModel).refresh();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLifecycle().addObserver(((LoadableMoviePageViewModel) mViewModel));
    }

    @Override
    protected void doDestroyView() {
        getLifecycle().removeObserver(((LoadableMoviePageViewModel) mViewModel));
        super.doDestroyView();
    }
}
