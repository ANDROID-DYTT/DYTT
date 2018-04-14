package com.bzh.dytt;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.CategoryPage;
import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.CategoryMapDAO;
import com.bzh.dytt.data.db.CategoryPageDAO;
import com.bzh.dytt.data.db.DatabaseResource;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.network.ApiResponse;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.data.network.NetworkBoundResource;
import com.bzh.dytt.data.network.NetworkResource;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.task.FetchSearchVideoDetailTask;
import com.bzh.dytt.task.FetchVideoDetailTask;
import com.bzh.dytt.util.CategoryPageParser;
import com.bzh.dytt.util.RateLimiter;
import com.bzh.dytt.util.VideoDetailPageParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;

@Singleton
public class DataRepository {

    private AppExecutors mAppExecutors;
    private DyttService mService;
    private CategoryMapDAO mCategoryMapDAO;
    private CategoryPageDAO mCategoryPageDAO;
    private VideoDetailDAO mVideoDetailDAO;
    private CategoryPageParser mCategoryPageParser;
    private VideoDetailPageParser mVideoDetailPageParser;

    private RateLimiter<String> mRepoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    private MutableLiveData<Throwable> mFetchDetailState = new MutableLiveData<>();

    @Inject
    DataRepository(AppExecutors appExecutors, DyttService service, CategoryMapDAO categoryMapDAO, CategoryPageDAO categoryPageDAO, VideoDetailDAO videoDetailDAO, CategoryPageParser categoryPageParser, VideoDetailPageParser videoDetailPageParser) {
        mAppExecutors = appExecutors;
        mService = service;
        mCategoryMapDAO = categoryMapDAO;
        mCategoryPageDAO = categoryPageDAO;
        mVideoDetailDAO = videoDetailDAO;
        mCategoryPageParser = categoryPageParser;
        mVideoDetailPageParser = videoDetailPageParser;
    }

    public LiveData<Resource<List<CategoryMap>>> getMovieListByCategory(final MovieCategory movieCategory) {
        return new NetworkBoundResource<List<CategoryMap>, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                try {

                    String html = new String(responseBody.bytes(), "GB2312");
                    List<CategoryMap> categoryMaps = mCategoryPageParser.parse(html, movieCategory);
                    mCategoryMapDAO.insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        details.add(videoDetail.updateValue(category));
                    }
                    mVideoDetailDAO.insertVideoDetailList(details);

                    mCategoryPageDAO.insertPage(movieCategory.getDefaultPage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CategoryMap> data) {
                return data == null || data.isEmpty() || mRepoListRateLimit.shouldFetch("MOVIE_LIST_" + movieCategory.getTitle());
            }

            @NonNull
            @Override
            protected LiveData<List<CategoryMap>> loadFromDb() {
                return mCategoryMapDAO.getMovieLinksByCategory(movieCategory);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                return mService.getMovieListByCategory(movieCategory.getDefaultUrl());
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<CategoryMap>>> search(final MovieCategory movieCategory, final String query) {
        return new NetworkBoundResource<List<CategoryMap>, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                try {
                    String html = new String(responseBody.bytes(), "GB2312");

                    List<CategoryMap> categoryMaps = mCategoryPageParser.parse(html, movieCategory);
                    for (CategoryMap category : categoryMaps) {
                        category.setQuery(query);
                    }
                    mCategoryMapDAO.insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        details.add(videoDetail.updateValue(category));
                    }
                    mVideoDetailDAO.insertVideoDetailList(details);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CategoryMap> data) {
                return data == null || data.isEmpty() || mRepoListRateLimit.shouldFetch("MOVIE_LIST_" + movieCategory.getTitle());
            }

            @NonNull
            @Override
            protected LiveData<List<CategoryMap>> loadFromDb() {
                return mCategoryMapDAO.getMovieLinksByCategoryAndQuery(movieCategory, query);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                String url = String.format("http://s.ygdy8.com/plus/so.php?kwtype=0&searchtype=title&keyword=%s", query);
                return mService.search(url);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<CategoryMap>>> getNextMovieListByCategory(final MovieCategory movieCategory) {
        return new NetworkResource<List<CategoryMap>, ResponseBody>(mAppExecutors) {

            private CategoryPage mNextPage;

            @Override
            protected void onPreProcess() {
                super.onPreProcess();
                mNextPage = mCategoryPageDAO.nextPage(movieCategory);
            }

            @Override
            protected List<CategoryMap> saveCallResult(@NonNull ResponseBody item) {
                try {
                    String html = new String(item.bytes(), "GB2312");

                    List<CategoryMap> categoryMaps = mCategoryPageParser.parse(html, movieCategory);
                    mCategoryMapDAO.insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        videoDetail.updateValue(category);
                        details.add(videoDetail);
                    }
                    mVideoDetailDAO.insertVideoDetailList(details);

                    mCategoryPageDAO.updatePage(mNextPage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Collections.emptyList();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                return mService.getMovieListByCategory(movieCategory.getNextPageUrl(mNextPage));
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetailsByCategory(final MovieCategory category) {
        return new DatabaseResource<List<VideoDetail>>(mAppExecutors) {

            @NonNull
            @Override
            protected void processDBData(List<VideoDetail> newData) {
                super.processDBData(newData);

                for (VideoDetail videoDetail : newData) {
                    boolean isValid = mVideoDetailDAO.isValid(videoDetail.getDetailLink(), category);
                    if (!isValid) {
                        FetchVideoDetailTask task = new FetchVideoDetailTask(videoDetail, mVideoDetailDAO, mService, mVideoDetailPageParser, mFetchDetailState);
                        mAppExecutors.networkIO().execute(task);
                    }
                }
            }

            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mVideoDetailDAO.getVideoDetailsByCategory(category);

            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetails(final List<String> detailLinks) {
        return new DatabaseResource<List<VideoDetail>>(mAppExecutors) {
            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mVideoDetailDAO.getVideoDetails(detailLinks.toArray(new String[]{}));

            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetailsByCategoryAndQuery(final MovieCategory category, final String query) {
        return new DatabaseResource<List<VideoDetail>>(mAppExecutors) {
            @NonNull
            @Override
            protected void processDBData(List<VideoDetail> newData) {
                super.processDBData(newData);
                for (VideoDetail videoDetail : newData) {
                    boolean isValid = mVideoDetailDAO.isValid(videoDetail.getDetailLink(), category);
                    if (!isValid) {
                        FetchSearchVideoDetailTask task = new FetchSearchVideoDetailTask(videoDetail, mVideoDetailDAO, mService, mVideoDetailPageParser, mFetchDetailState);
                        mAppExecutors.networkIO().execute(task);
                    }
                }
            }

            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mVideoDetailDAO.getVideoDetailsByCategoryAndQuery(category, query);

            }
        }.getAsLiveData();
    }

    public LiveData<Throwable> getFetchVideoDetailState() {
        return mFetchDetailState;
    }
}
