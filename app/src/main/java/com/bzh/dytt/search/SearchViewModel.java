package com.bzh.dytt.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.network.Resource;

import java.util.List;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final DataRepository mRepository;

    @Inject
    public SearchViewModel(DataRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<List<HomeItem>>> toSearch(String text) {
        return mRepository.getSearchResult(text);
    }
}
