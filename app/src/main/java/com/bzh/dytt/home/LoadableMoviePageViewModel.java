package com.bzh.dytt.home;


import android.arch.core.util.Function;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.bzh.dytt.BaseViewModel;
import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.data.network.Status;

import java.util.List;

import javax.inject.Inject;

public class LoadableMoviePageViewModel extends BaseViewModel implements LifecycleObserver {

    private LiveData<Resource<List<VideoDetail>>> mVideoList;
    private MovieCategory mCategory;
    private CategoryHandler mCategoryHandler;

    @Inject
    LoadableMoviePageViewModel(DataRepository repository) {
        super(repository);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void active() {
        if (mCategoryHandler != null) {
            mCategoryHandler.refreshPage();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void inactive() {
        if (mCategoryHandler != null) {
            mCategoryHandler.unregister();
        }
    }

    public void setCategory(MovieCategory category) {
        mCategory = category;
        mCategoryHandler = new CategoryHandler(mDataRepository, mCategory);
        mVideoList = Transformations.switchMap(mCategoryHandler.getCategoryMap(), new Function<Resource<List<CategoryMap>>, LiveData<Resource<List<VideoDetail>>>>() {
            @Override
            public LiveData<Resource<List<VideoDetail>>> apply(Resource<List<CategoryMap>> categoryMaps) {
                return mDataRepository.getVideoDetailsByCategory(mCategory);
            }
        });
    }

    LiveData<Throwable> getFetchVideoDetailState() {
        return mDataRepository.getFetchVideoDetailState();
    }

    void refresh() {
        mCategoryHandler.refreshPage();
    }

    void loadNextPage() {
        mCategoryHandler.nextPage();
    }

    LiveData<Resource<List<VideoDetail>>> getMovieList() {
        return mVideoList;
    }

    static class CategoryHandler implements Observer<Resource<List<CategoryMap>>> {

        private MutableLiveData<Resource<List<CategoryMap>>> mCategoryMap = new MutableLiveData<>();

        private LiveData<Resource<List<CategoryMap>>> mLiveData;

        private DataRepository mRepository;

        private MovieCategory mMovieCategory;

        private boolean mIsRunning;

        CategoryHandler(DataRepository repository, MovieCategory movieCategory) {
            mRepository = repository;
            mMovieCategory = movieCategory;
        }

        @Override
        public void onChanged(@Nullable Resource<List<CategoryMap>> result) {
            if (result == null) {
                unregister();
            } else {
                if (result.status == Status.SUCCESS || result.status == Status.ERROR) {
                    mCategoryMap.setValue(result);
                    unregister();
                }
            }
        }

        private void unregister() {
            if (mLiveData != null) {
                mLiveData.removeObserver(this);
                mIsRunning = false;
                mLiveData = null;
            }
        }

        void refreshPage() {
            if (mIsRunning) {
                return;
            }
            unregister();
            mLiveData = mRepository.getMovieListByCategory(mMovieCategory);
            mLiveData.observeForever(this);
        }

        void nextPage() {
            if (mIsRunning) {
                return;
            }
            mIsRunning = true;
            unregister();
            mLiveData = mRepository.getNextMovieListByCategory(mMovieCategory);
            mLiveData.observeForever(this);
        }

        MutableLiveData<Resource<List<CategoryMap>>> getCategoryMap() {
            return mCategoryMap;
        }
    }
}
