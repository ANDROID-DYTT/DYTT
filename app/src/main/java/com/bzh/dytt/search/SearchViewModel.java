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
import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.util.AbsentLiveData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

        mCategoryList = Transformations.switchMap(mQuery, new Function<String, LiveData<Resource<List<CategoryMap>>>>() {

            @Override
            public LiveData<Resource<List<CategoryMap>>> apply(String search) {
                if (search == null || search.trim().length() == 0) {
                    return AbsentLiveData.create();
                } else {
                    return mRepository.search(MovieCategory.SEARCH_MOVIE, search);
                }
            }
        });
        mVideoList = Transformations.switchMap(mCategoryList, new Function<Resource<List<CategoryMap>>, LiveData<Resource<List<VideoDetail>>>>() {
            @Override
            public LiveData<Resource<List<VideoDetail>>> apply(Resource<List<CategoryMap>> categoryMaps) {
                return mRepository.getVideoDetailsByCategoryAndQuery(MovieCategory.SEARCH_MOVIE, mQuery.getValue());
            }
        });
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoList() {
        return mVideoList;
    }

    public void setQuery(@NonNull String originalInput) {
        String input = originalInput.toLowerCase(Locale.getDefault()).trim();
        if (TextUtils.equals(input, mQuery.getValue())) {
            return;
        }
        try {
            mQuery.setValue(URLEncoder.encode(input, "GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
