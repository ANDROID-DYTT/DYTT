package com.bzh.dytt.search;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bzh.dytt.BaseViewModel;
import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.ExceptionType;
import com.bzh.dytt.data.Resource;
import com.bzh.dytt.data.Status;
import com.bzh.dytt.data.entity.CategoryMap;
import com.bzh.dytt.data.entity.MovieCategory;
import com.bzh.dytt.data.entity.VideoDetail;
import com.bzh.dytt.viewmodel.VideoDetailHandle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SearchViewModel extends BaseViewModel {

    private final CategoryHandler mCategoryHandler;
    private final SearchVideoDetailHandle mVideoDetailHandle;
    private LiveData<Resource<List<VideoDetail>>> mVideoList;

    @Inject
    SearchViewModel(DataRepository repository) {
        super(repository);
        mVideoDetailHandle = new SearchVideoDetailHandle(getDataRepository());
        mCategoryHandler = new CategoryHandler(getDataRepository());

        mVideoList = Transformations.switchMap(mCategoryHandler.getCategoryMap(), new Function<Resource<List<CategoryMap>>, LiveData<Resource<List<VideoDetail>>>>() {
            @Override
            public LiveData<Resource<List<VideoDetail>>> apply(Resource<List<CategoryMap>> categoryMaps) {
                return getDataRepository().getVideoDetailsByCategoryAndQuery(MovieCategory.SEARCH_MOVIE, mCategoryHandler.getQuery());
            }
        });
    }

    LiveData<Resource<List<VideoDetail>>> getVideoList() {
        return mVideoList;
    }

    LiveData<Resource<ExceptionType>> getFetchVideoDetailState() {
        return getDataRepository().getFetchVideoDetailState();
    }

    MutableLiveData<VideoDetail> getVideoDetailLiveData() {
        return mVideoDetailHandle.getVideoDetail();
    }

    void setQuery(@NonNull String originalInput) {
        try {
            String input = originalInput.toLowerCase(Locale.getDefault()).trim();
            mCategoryHandler.setQuery(URLEncoder.encode(input, "GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static class SearchVideoDetailHandle extends VideoDetailHandle {

        private DataRepository mDataRepository;

        public SearchVideoDetailHandle(DataRepository dataRepository) {
            super(dataRepository);
            mDataRepository = dataRepository;
        }

        @Override
        public void onChanged(@Nullable VideoDetail videoDetail) {
            if (videoDetail != null && !videoDetail.isValidVideoItem()) {
                mDataRepository.parseSearchVideoDetail(videoDetail, MovieCategory.SEARCH_MOVIE);
            }
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
