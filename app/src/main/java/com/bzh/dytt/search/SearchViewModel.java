package com.bzh.dytt.search;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bzh.dytt.BaseViewModel;
import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.data.network.Status;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SearchViewModel extends BaseViewModel {

    private final CategoryHandler mCategoryHandler;
    private LiveData<Resource<List<VideoDetail>>> mVideoList;

    @Inject
    SearchViewModel(DataRepository repository) {
        super(repository);

        mCategoryHandler = new CategoryHandler(mDataRepository);

        mVideoList = Transformations.switchMap(mCategoryHandler.getCategoryMap(), new Function<Resource<List<CategoryMap>>, LiveData<Resource<List<VideoDetail>>>>() {
            @Override
            public LiveData<Resource<List<VideoDetail>>> apply(Resource<List<CategoryMap>> categoryMaps) {
                return mDataRepository.getVideoDetailsByCategoryAndQuery(MovieCategory.SEARCH_MOVIE, mCategoryHandler.getQuery());
            }
        });
    }

    LiveData<Resource<List<VideoDetail>>> getVideoList() {
        return mVideoList;
    }

    LiveData<Throwable> getFetchVideoDetailState() {
        return mDataRepository.getFetchVideoDetailState();
    }

    void setQuery(@NonNull String originalInput) {
        try {
            String input = originalInput.toLowerCase(Locale.getDefault()).trim();
            mCategoryHandler.setQuery(URLEncoder.encode(input, "GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static class CategoryHandler implements Observer<Resource<List<CategoryMap>>> {

        private MutableLiveData<Resource<List<CategoryMap>>> mCategoryMap = new MutableLiveData<>();

        private LiveData<Resource<List<CategoryMap>>> mLiveData;

        private DataRepository mRepository;
        private String mQuery;

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

        MutableLiveData<Resource<List<CategoryMap>>> getCategoryMap() {
            return mCategoryMap;
        }

        public String getQuery() {
            return mQuery;
        }

        public void setQuery(@NonNull String input) {
            if (TextUtils.equals(input, mQuery)) {
                return;
            }
            unregister();
            mQuery = input;
            mLiveData = mRepository.search(MovieCategory.SEARCH_MOVIE, mQuery);
            mLiveData.observeForever(this);
        }
    }
}
