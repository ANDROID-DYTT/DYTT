package com.bzh.dytt.viewmodel;


import android.arch.core.util.Function;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.Transformations;

import com.bzh.dytt.BaseViewModel;
import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.ExceptionType;
import com.bzh.dytt.data.Resource;
import com.bzh.dytt.data.entity.CategoryMap;
import com.bzh.dytt.data.entity.MovieCategory;
import com.bzh.dytt.data.entity.VideoDetail;

import java.util.List;

import javax.inject.Inject;

public class LoadableMoviePageViewModel extends BaseViewModel implements LifecycleObserver {

    private LiveData<Resource<List<VideoDetail>>> mVideoList;
    private MovieCategory mCategory;
    private CategoryHandler mCategoryHandler;
    private VideoDetailHandle mVideoDetailHandle;

    @Inject
    LoadableMoviePageViewModel(DataRepository repository) {
        super(repository);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void active() {
        if (mVideoDetailHandle != null) {
            mVideoDetailHandle.register();
        }
        if (mCategoryHandler != null) {
            mCategoryHandler.refresh();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void inactive() {
        if (mVideoDetailHandle != null) {
            mVideoDetailHandle.unregister();
        }
        if (mCategoryHandler != null) {
            mCategoryHandler.unregister();
        }
    }

    public void setCategory(MovieCategory category) {
        mCategory = category;
        mVideoDetailHandle = new VideoDetailHandle(getDataRepository());
        mCategoryHandler = new CategoryHandler(getDataRepository(), mCategory);
        mVideoList = Transformations.switchMap(mCategoryHandler.getCategoryMap(), new Function<Resource<List<CategoryMap>>, LiveData<Resource<List<VideoDetail>>>>() {
            @Override
            public LiveData<Resource<List<VideoDetail>>> apply(Resource<List<CategoryMap>> categoryMaps) {
                return getDataRepository().getVideoDetailsByCategory(mCategory);
            }
        });
    }

    public MutableLiveData<VideoDetail> getVideoDetailLiveData() {
        return mVideoDetailHandle.getVideoDetail();
    }

    public LiveData<Resource<ExceptionType>> getFetchVideoDetailState() {
        return getDataRepository().getFetchVideoDetailState();
    }

    public void refresh() {
        mCategoryHandler.refresh();
    }

    public void loadNextPage() {
        mCategoryHandler.nextPage();
    }

    public LiveData<Resource<List<VideoDetail>>> getMovieList() {
        return mVideoList;
    }


}
