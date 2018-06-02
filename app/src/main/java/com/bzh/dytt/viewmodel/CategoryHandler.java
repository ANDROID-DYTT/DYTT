package com.bzh.dytt.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.Resource;
import com.bzh.dytt.data.Status;
import com.bzh.dytt.data.entity.CategoryMap;
import com.bzh.dytt.data.entity.MovieCategory;

import java.util.List;

public class CategoryHandler implements Observer<Resource<List<CategoryMap>>> {

    private MutableLiveData<Resource<List<CategoryMap>>> mCategoryMap = new MutableLiveData<>();

    private LiveData<Resource<List<CategoryMap>>> mLiveData;

    private DataRepository mRepository;

    private MovieCategory mMovieCategory;

    private boolean mIsRunning;

    public CategoryHandler(DataRepository repository, MovieCategory movieCategory) {
        mRepository = repository;
        mMovieCategory = movieCategory;
    }

    @Override
    public void onChanged(@Nullable Resource<List<CategoryMap>> result) {
        if (result == null) {
            unregister();
        } else {
            if (result.getStatus() == Status.SUCCESS || result.getStatus() == Status.ERROR) {
                mCategoryMap.setValue(result);
                unregister();
            }
        }
    }

    public void unregister() {
        if (mLiveData != null) {
            mLiveData.removeObserver(this);
            mIsRunning = false;
            mLiveData = null;
        }
    }

    public void refresh() {
        if (mIsRunning) {
            return;
        }
        unregister();
        mLiveData = mRepository.getMovieListByCategory(mMovieCategory);
        mLiveData.observeForever(this);
    }

    public void nextPage() {
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        unregister();
        mLiveData = mRepository.getNextMovieListByCategory(mMovieCategory);
        mLiveData.observeForever(this);
    }

    public MutableLiveData<Resource<List<CategoryMap>>> getCategoryMap() {
        return mCategoryMap;
    }
}