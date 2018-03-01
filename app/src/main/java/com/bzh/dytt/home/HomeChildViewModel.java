package com.bzh.dytt.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.network.Resource;

import java.util.List;

import javax.inject.Inject;

public class HomeChildViewModel extends ViewModel {

    private DataRepository mRepository;

    @Inject
    HomeChildViewModel(DataRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<List<HomeItem>>> getItemsByType(int type) {
        return mRepository.getHomeItems(type);
    }
}
