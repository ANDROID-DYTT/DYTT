package com.bzh.dytt;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.CategoryPage;
import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.AppDatabase;
import com.bzh.dytt.data.db.DatabaseResource;
import com.bzh.dytt.data.network.ApiResponse;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.data.network.NetworkBoundResource;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.task.FetchSearchVideoDetailTask;
import com.bzh.dytt.task.FetchVideoDetailTask;
import com.bzh.dytt.util.HomePageParser;
import com.bzh.dytt.util.LoadableMovieParser;
import com.bzh.dytt.util.RateLimiter;
import com.bzh.dytt.util.VideoDetailPageParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Response;

@Singleton
public class DataRepository {

    private AppExecutors mAppExecutors;

    private DyttService mService;

    private AppDatabase mAppDatabase;

    private HomePageParser mHomePageParser;

    private LoadableMovieParser mLoadableMovieParser;

    private VideoDetailPageParser mVideoDetailPageParser;

    private RateLimiter<String> mRepoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    DataRepository(AppExecutors appExecutors, DyttService service, AppDatabase appDatabase, HomePageParser
            homePageParser, VideoDetailPageParser videoDetailPageParser, LoadableMovieParser loadableMovieParser) {
        mAppExecutors = appExecutors;
        mService = service;
        mAppDatabase = appDatabase;
        mHomePageParser = homePageParser;
        mLoadableMovieParser = loadableMovieParser;
        mVideoDetailPageParser = videoDetailPageParser;
    }

    public LiveData<Resource<List<CategoryMap>>> getLatestMovie() {
        return new NetworkBoundResource<List<CategoryMap>, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                try {

                    String item = new String(responseBody.bytes(), "GB2312");

                    List<CategoryMap> categoryMaps = mHomePageParser.parseLatestMovieCategoryMap(item);
                    mAppDatabase.categoryMapDAO().insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        videoDetail.setDetailLink(category.getLink());
                        videoDetail.setSN(category.getSN());
                        videoDetail.setCategory(category.getCategory());
                        details.add(videoDetail);
                    }
                    mAppDatabase.videoDetailDAO().insertVideoDetailList(details);

                    for (CategoryMap category : categoryMaps) {
                        boolean isParsed = mAppDatabase.categoryMapDAO().IsParsed(category.getLink());
                        if (!isParsed) {
                            getVideoDetailNew(category);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CategoryMap> data) {
                return data == null || data.isEmpty() || mRepoListRateLimit.shouldFetch("LATEST_MOVIE");
            }

            @NonNull
            @Override
            protected LiveData<List<CategoryMap>> loadFromDb() {
                return mAppDatabase.categoryMapDAO().getMovieLinksByCategory(MovieCategory.HOME_LATEST_MOVIE);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> createCall() {
                return mService.getHomePage();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<CategoryMap>>> getMovieListByCategory(final MovieCategory movieCategory) {
        return new NetworkBoundResource<List<CategoryMap>, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                try {
                    String item = new String(responseBody.bytes(), "GB2312");
                    List<CategoryMap> categoryMaps = mLoadableMovieParser.getMovieList(item, movieCategory);
                    mAppDatabase.categoryMapDAO().insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        videoDetail.setDetailLink(category.getLink());
                        videoDetail.setSN(category.getSN());
                        videoDetail.setCategory(category.getCategory());
                        details.add(videoDetail);
                    }
                    mAppDatabase.videoDetailDAO().insertVideoDetailList(details);

                    for (CategoryMap category : categoryMaps) {
                        boolean isParsed = mAppDatabase.categoryMapDAO().IsParsed(category.getLink());
                        if (!isParsed) {
                            getVideoDetailNew(category);
                        }
                    }

                    mAppDatabase.categoryPageDAO().insertPage(movieCategory.getDefaultPage());
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
                return mAppDatabase.categoryMapDAO().getMovieLinksByCategory(movieCategory);
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
                    String item = new String(responseBody.bytes(), "GB2312");
                    List<CategoryMap> categoryMaps = mLoadableMovieParser.getMovieList(item, movieCategory);
                    for (CategoryMap category : categoryMaps) {
                        category.setQuery(query);
                    }
                    mAppDatabase.categoryMapDAO().insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        videoDetail.setDetailLink(category.getLink());
                        videoDetail.setSN(category.getSN());
                        videoDetail.setQuery(query);
                        videoDetail.setCategory(category.getCategory());
                        details.add(videoDetail);
                    }
                    mAppDatabase.videoDetailDAO().insertVideoDetailList(details);

                    for (CategoryMap category : categoryMaps) {
                        boolean isParsed = mAppDatabase.categoryMapDAO().IsParsed(category.getLink());
                        if (!isParsed) {
                            getSearchVideoDetailNew(category, query);
                        }
                    }
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
                return mAppDatabase.categoryMapDAO().getMovieLinksByCategoryAndQuery(movieCategory, query);
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
        final MutableLiveData<Resource<List<CategoryMap>>> liveData = new MutableLiveData<>();
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CategoryPage nextPage = mAppDatabase.categoryPageDAO().nextPage(movieCategory.ordinal());
                    Response<ResponseBody> response = mService.getMovieListByCategory2(movieCategory.getNextPageUrl(nextPage)).execute();
                    ApiResponse<ResponseBody> apiResponse = new ApiResponse<>(response);
                    if (apiResponse.isSuccessful()) {

                        String item = new String(apiResponse.body.bytes(), "GB2312");
                        List<CategoryMap> categoryMaps = mLoadableMovieParser.getMovieList(item, movieCategory);
                        mAppDatabase.categoryMapDAO().insertCategoryMapList(categoryMaps);

                        List<VideoDetail> details = new ArrayList<>();
                        for (CategoryMap category : categoryMaps) {
                            VideoDetail videoDetail = new VideoDetail();
                            videoDetail.setDetailLink(category.getLink());
                            videoDetail.setSN(category.getSN());
                            videoDetail.setCategory(category.getCategory());
                            details.add(videoDetail);
                        }
                        mAppDatabase.videoDetailDAO().insertVideoDetailList(details);

                        mAppDatabase.categoryPageDAO().updatePage(nextPage);

                        for (CategoryMap category : categoryMaps) {
                            boolean isParsed = mAppDatabase.categoryMapDAO().IsParsed(category.getLink());
                            if (!isParsed) {
                                getVideoDetailNew(category);
                            }
                        }

                        liveData.postValue(Resource.success(categoryMaps));
                    } else {
                        liveData.postValue(Resource.<List<CategoryMap>>error(apiResponse.errorMessage, null));
                    }
                } catch (Exception e) {
                    liveData.postValue(Resource.<List<CategoryMap>>error(e.getMessage(), null));
                }
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetailsByCategory(final MovieCategory category) {
        return new DatabaseResource<List<VideoDetail>>(mAppExecutors) {
            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mAppDatabase.videoDetailDAO().getVideoDetailsByCategory(category);

            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetails(final List<String> detailLinks) {
        return new DatabaseResource<List<VideoDetail>>(mAppExecutors) {
            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mAppDatabase.videoDetailDAO().getVideoDetails(detailLinks.toArray(new String[]{}));

            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetailsByCategoryAndQuery(final MovieCategory category, final String query) {
        return new DatabaseResource<List<VideoDetail>>(mAppExecutors) {
            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mAppDatabase.videoDetailDAO().getVideoDetailsByCategoryAndQuery(category, query);

            }
        }.getAsLiveData();
    }

    public void getVideoDetailNew(CategoryMap categoryMap) {
        FetchVideoDetailTask task = new FetchVideoDetailTask(categoryMap, mAppDatabase, mService, mVideoDetailPageParser);
        mAppExecutors.networkIO().execute(task);
    }

    public void getSearchVideoDetailNew(CategoryMap categoryMap, String query) {
        FetchSearchVideoDetailTask task = new FetchSearchVideoDetailTask(categoryMap, mAppDatabase, mService, mVideoDetailPageParser, query);
        mAppExecutors.networkIO().execute(task);
    }

}
