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

public class NewMovieViewModel extends BaseViewModel implements LifecycleObserver {

    private final CategoryHandler mCategoryHandler;
    private LiveData<Resource<List<VideoDetail>>> mVideoList;

    @Inject
    NewMovieViewModel(DataRepository repository) {
        super(repository);
        mCategoryHandler = new CategoryHandler(mDataRepository);
        mVideoList = Transformations.switchMap(mCategoryHandler.getCategoryMap(), new Function<Resource<List<CategoryMap>>, LiveData<Resource<List<VideoDetail>>>>() {
            @Override
            public LiveData<Resource<List<VideoDetail>>> apply(Resource<List<CategoryMap>> result) {
                return mDataRepository.getVideoDetailsByCategory(MovieCategory.HOME_LATEST_MOVIE);
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void active() {
        if (mCategoryHandler != null) {
            mCategoryHandler.refresh();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void inactive() {
        if (mCategoryHandler != null) {
            mCategoryHandler.unregister();
        }
    }

    @VisibleForTesting
    void refresh() {
        mCategoryHandler.refresh();
    }

    @VisibleForTesting
    LiveData<Resource<List<VideoDetail>>> getNewMovieList() {
        return mVideoList;
    }

    static class CategoryHandler implements Observer<Resource<List<CategoryMap>>> {

        private MutableLiveData<Resource<List<CategoryMap>>> mCategoryMap = new MutableLiveData<>();

        private LiveData<Resource<List<CategoryMap>>> mLiveData;

        private DataRepository mRepository;

        CategoryHandler(DataRepository repository) {
            mRepository = repository;
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
                mLiveData = null;
            }
        }

        void refresh() {
            unregister();
            mLiveData = mRepository.getMovieListByCategory(MovieCategory.HOME_LATEST_MOVIE);
            mLiveData.observeForever(this);
        }

        MutableLiveData<Resource<List<CategoryMap>>> getCategoryMap() {
            return mCategoryMap;
        }
    }
}
