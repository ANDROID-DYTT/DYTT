package com.bzh.dytt.home;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.source.Resource;

import javax.inject.Inject;

public class VideoDetailPageViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    @Inject
    public VideoDetailPageViewModel(@NonNull Application application, DataRepository repository) {
        super(application);
        mRepository = repository;
    }

    public LiveData<Resource<VideoDetail>> getVideoDetail(String detailLink) {
        return mRepository.getVideoDetail(detailLink);
    }

}
