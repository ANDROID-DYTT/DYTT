package com.bzh.dytt;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.source.ApiResponse;
import com.bzh.dytt.data.source.AppDatabase;
import com.bzh.dytt.data.source.DyttService;
import com.bzh.dytt.util.HomePageParser;
import com.bzh.dytt.data.source.NetworkBoundResource;
import com.bzh.dytt.data.source.Resource;
import com.bzh.dytt.util.VideoDetailPageParser;
import com.bzh.dytt.util.RateLimiter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;

@Singleton
public class DataRepository {

    private AppExecutors mAppExecutors;
    private DyttService mService;
    private AppDatabase mAppDatabase;
    private HomePageParser mHomePageParser;
    private VideoDetailPageParser mVideoDetailPageParser;

    @Inject
    DataRepository(AppExecutors appExecutors, DyttService service, AppDatabase appDatabase, HomePageParser
            homePageParser, VideoDetailPageParser videoDetailPageParser) {
        mAppExecutors = appExecutors;
        mService = service;
        mAppDatabase = appDatabase;
        mHomePageParser = homePageParser;
        mVideoDetailPageParser = videoDetailPageParser;
    }

    private RateLimiter<String> mRepoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    /**
     * 获取电影天堂首页的各个区域的信息
     */
    public LiveData<Resource<List<HomeArea>>> getHomeAreas() {
        return new NetworkBoundResource<List<HomeArea>, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                try {
                    String item = new String(responseBody.bytes(), "GB2312");
                    List<HomeArea> homeAreas = mHomePageParser.parseAreas(item);
                    mAppDatabase.homeAreaDAO().insertAreas(homeAreas);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<HomeArea> data) {
                return data == null || data.isEmpty() || mRepoListRateLimit.shouldFetch("HOME_AREA");
            }

            @NonNull
            @Override
            protected LiveData<List<HomeArea>> loadFromDb() {
                return mAppDatabase.homeAreaDAO().getAreas();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                return mService.getHomePage();
            }
        }.getAsLiveData();
    }


    /**
     * 获取各个区域里的具体条目信息
     * @param type 区域 ID {@link HomeType}
     */
    public LiveData<Resource<List<HomeItem>>> getHomeItems(final int type) {
        return new NetworkBoundResource<List<HomeItem>, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                try {
                    String item = new String(responseBody.bytes(), "GB2312");
                    List<HomeItem> homeItems = mHomePageParser.parseItems(item);
                    mAppDatabase.homeItemDAO().insertItems(homeItems);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<HomeItem> data) {
                return data == null || data.isEmpty() || mRepoListRateLimit.shouldFetch("HOME_ITEMS");
            }

            @NonNull
            @Override
            protected LiveData<List<HomeItem>> loadFromDb() {
                return mAppDatabase.homeItemDAO().getItemsByType(type);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                return mService.getHomePage();
            }

        }.getAsLiveData();
    }

    public LiveData<Resource<VideoDetail>> getVideoDetail(final String detailLink) {
        return new NetworkBoundResource<VideoDetail, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                String item = null;
                try {
                    item = new String(responseBody.bytes(), "GB2312");
                    VideoDetail videoDetail = mVideoDetailPageParser.parseVideoDetail(item);
                    videoDetail.setDetailLink(detailLink);
                    mAppDatabase.videoDetailDAO().insertVideoDetail(videoDetail);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable VideoDetail data) {
                return data == null || mRepoListRateLimit.shouldFetch("VIDEO_DETAIL");
            }

            @NonNull
            @Override
            protected LiveData<VideoDetail> loadFromDb() {
                LiveData<VideoDetail> tmp = mAppDatabase.videoDetailDAO().getVideoDetailByLink(detailLink);
                return tmp;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                return mService.getVideoDetail(detailLink);
            }
        }.getAsLiveData();
    }
}
