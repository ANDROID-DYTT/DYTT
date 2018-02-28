package com.bzh.dytt.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.network.Resource;

import java.util.List;

import javax.inject.Inject;

public class HomePageViewModel extends ViewModel {

    private final DataRepository mRepository;

    @Inject
    public HomePageViewModel(DataRepository repository) {
        mRepository = repository;
    }

    @VisibleForTesting
    public LiveData<Resource<List<HomeArea>>> getHomeArea() {
        return mRepository.getHomeAreas();
    }

    @VisibleForTesting
    public LiveData<Resource<List<HomeItem>>> getHomeItems(int type) {
        return mRepository.getHomeItems(type);
    }
}
