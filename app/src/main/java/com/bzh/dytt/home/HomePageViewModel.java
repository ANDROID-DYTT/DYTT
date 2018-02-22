package com.bzh.dytt.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.source.HomePageRepository;
import com.bzh.dytt.data.source.Resource;

import java.util.List;

public class HomePageViewModel extends AndroidViewModel {

    private final HomePageRepository mRepository;

    public HomePageViewModel(@NonNull Application application, HomePageRepository repository) {
        super(application);
        mRepository = repository;
    }

    public LiveData<Resource<List<HomeArea>>> getHomeArea() {
        return mRepository.getHomeAreas();
    }

    public LiveData<Resource<List<HomeItem>>> getHomeItems(int type) {
        return mRepository.getHomeItems(type);
    }
}
