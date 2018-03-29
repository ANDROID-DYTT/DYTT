package com.bzh.dytt.search;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final DataRepository mRepository;

    private final MutableLiveData<String> mQuery = new MutableLiveData<>();
    private LiveData<Resource<List<VideoDetail>>> mVideoList;
    private LiveData<Resource<List<CategoryMap>>> mCategoryList;

    @Inject
    SearchViewModel(DataRepository repository) {
        mRepository = repository;

//        mCategoryList = Transformations.switchMap(mQuery, new Function<String, LiveData<List<Resource<CategoryMap>>>>() {
//
//            @Override
//            public LiveData<List<Resource<CategoryMap>>> apply(String search) {
//                if (search == null || search.trim().length() == 0) {
//                    return AbsentLiveData.create();
//                } else {
////                    return mRepository.search(search);
//                    return AbsentLiveData.create();
//                }
//            }
//        });
        mVideoList = Transformations.switchMap(mCategoryList, new Function<Resource<List<CategoryMap>>, LiveData<Resource<List<VideoDetail>>>>() {
            @Override
            public LiveData<Resource<List<VideoDetail>>> apply(Resource<List<CategoryMap>> categoryMaps) {
                List<String> linkList = new ArrayList<>();
                if (categoryMaps != null && categoryMaps.data != null) {
                    for (CategoryMap categoryMap : categoryMaps.data) {
                        linkList.add(categoryMap.getLink());
                    }
                }
                return mRepository.getVideoDetails(linkList);
            }
        });
    }

    public void setQuery(@NonNull String originalInput) {
        String input = originalInput.toLowerCase(Locale.getDefault()).trim();
        if (TextUtils.equals(input, mQuery.getValue())) {
            return;
        }
        mQuery.setValue(input);
    }
}
