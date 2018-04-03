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
import com.bzh.dytt.data.db.DatabaseBoundResource;
import com.bzh.dytt.data.db.VideoDetailDAO;
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

    private CategoryMapDAO mCategoryMapDAO;

    private CategoryPageDAO mCategoryPageDAO;
    private VideoDetailDAO mVideoDetailDAO;
    private HomePageParser mHomePageParser;

    private LoadableMovieParser mLoadableMovieParser;

    private VideoDetailPageParser mVideoDetailPageParser;

    private RateLimiter<String> mRepoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    DataRepository(AppExecutors appExecutors, DyttService service, CategoryMapDAO categoryMapDAO, CategoryPageDAO categoryPageDAO, VideoDetailDAO videoDetailDAO, HomePageParser homePageParser, VideoDetailPageParser videoDetailPageParser, LoadableMovieParser loadableMovieParser) {
        mAppExecutors = appExecutors;
        mService = service;
        mCategoryMapDAO = categoryMapDAO;
        mCategoryPageDAO = categoryPageDAO;
        mVideoDetailDAO = videoDetailDAO;
        mHomePageParser = homePageParser;
        mLoadableMovieParser = loadableMovieParser;
        mVideoDetailPageParser = videoDetailPageParser;
    }

    public LiveData<Resource<List<CategoryMap>>> getLatestMovie() {
        return new NetworkBoundResource<List<CategoryMap>, ResponseBody>(mAppExecutors) {

            @Override
            protected void saveCallResult(@NonNull ResponseBody responseBody) {
                try {

                    String html = new String(responseBody.bytes(), "GB2312");

                    List<CategoryMap> categoryMaps = mHomePageParser.parseLatestMovieCategoryMap(html);
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
                return data == null || data.isEmpty() || mRepoListRateLimit.shouldFetch("LATEST_MOVIE");
            }

            @NonNull
            @Override
            protected LiveData<List<CategoryMap>> loadFromDb() {
                return mCategoryMapDAO.getMovieLinksByCategory(MovieCategory.HOME_LATEST_MOVIE);
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
                    List<CategoryMap> categoryMaps = mLoadableMovieParser.parseCategoryMap(item, movieCategory);
                    mCategoryMapDAO.insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        videoDetail.setDetailLink(category.getLink());
                        videoDetail.setSN(category.getSN());
                        videoDetail.setCategory(category.getCategory());
                        details.add(videoDetail);
                    }
                    mVideoDetailDAO.insertVideoDetailList(details);
//
//                    for (CategoryMap category : categoryMaps) {
//                        boolean isParsed = mCategoryMapDAO.IsParsed(category.getLink());
//                        if (!isParsed) {
//                            getVideoDetailNew(category);
//                        }
//                    }

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
                    String item = new String(responseBody.bytes(), "GB2312");
                    List<CategoryMap> categoryMaps = mLoadableMovieParser.parseCategoryMap(item, movieCategory);
                    for (CategoryMap category : categoryMaps) {
                        category.setQuery(query);
                    }
                    mCategoryMapDAO.insertCategoryMapList(categoryMaps);

                    List<VideoDetail> details = new ArrayList<>();
                    for (CategoryMap category : categoryMaps) {
                        VideoDetail videoDetail = new VideoDetail();
                        videoDetail.setDetailLink(category.getLink());
                        videoDetail.setSN(category.getSN());
                        videoDetail.setQuery(query);
                        videoDetail.setCategory(category.getCategory());
                        details.add(videoDetail);
                    }
                    mVideoDetailDAO.insertVideoDetailList(details);

//                    for (CategoryMap category : categoryMaps) {
//                        boolean isParsed = mCategoryMapDAO.IsParsed(category.getLink());
//                        if (!isParsed) {
//                            getSearchVideoDetailNew(category, query);
//                        }
//                    }
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
        final MutableLiveData<Resource<List<CategoryMap>>> liveData = new MutableLiveData<>();
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CategoryPage nextPage = mCategoryPageDAO.nextPage(movieCategory.ordinal());
                    Response<ResponseBody> response = mService.getMovieListByCategory2(movieCategory.getNextPageUrl(nextPage)).execute();
                    ApiResponse<ResponseBody> apiResponse = new ApiResponse<>(response);
                    if (apiResponse.isSuccessful()) {

                        String item = new String(apiResponse.body.bytes(), "GB2312");
                        List<CategoryMap> categoryMaps = mLoadableMovieParser.parseCategoryMap(item, movieCategory);
                        mCategoryMapDAO.insertCategoryMapList(categoryMaps);

                        List<VideoDetail> details = new ArrayList<>();
                        for (CategoryMap category : categoryMaps) {
                            VideoDetail videoDetail = new VideoDetail();
                            videoDetail.setDetailLink(category.getLink());
                            videoDetail.setSN(category.getSN());
                            videoDetail.setCategory(category.getCategory());
                            details.add(videoDetail);
                        }
                        mVideoDetailDAO.insertVideoDetailList(details);

                        mCategoryPageDAO.updatePage(nextPage);

//                        for (CategoryMap category : categoryMaps) {
//                            boolean isParsed = mCategoryMapDAO.IsParsed(category.getLink());
//                            if (!isParsed) {
//                                getVideoDetailNew(category);
//                            }
//                        }

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
        return new DatabaseBoundResource<List<VideoDetail>>(mAppExecutors) {
            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mVideoDetailDAO.getVideoDetailsByCategory(category);

            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetails(final List<String> detailLinks) {
        return new DatabaseBoundResource<List<VideoDetail>>(mAppExecutors) {

            @NonNull
            @Override
            protected void processDBData(List<VideoDetail> newData) {
                super.processDBData(newData);

                for (VideoDetail videoDetail : newData) {
                    boolean isValid = mVideoDetailDAO.IsValid(videoDetail.getDetailLink());
                    if (!isValid) {
                        FetchVideoDetailTask task = new FetchVideoDetailTask(videoDetail, mVideoDetailDAO, mService, mVideoDetailPageParser);
                        mAppExecutors.networkIO().execute(task);
                    }
                }
            }

            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mVideoDetailDAO.getVideoDetails(detailLinks.toArray(new String[]{}));

            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<VideoDetail>>> getVideoDetailsByCategoryAndQuery(final MovieCategory category, final String query) {
        return new DatabaseBoundResource<List<VideoDetail>>(mAppExecutors) {
            @NonNull
            @Override
            protected LiveData<List<VideoDetail>> loadFromDb() {
                return mVideoDetailDAO.getVideoDetailsByCategoryAndQuery(category, query);

            }
        }.getAsLiveData();
    }

    public void getSearchVideoDetailNew(CategoryMap categoryMap, String query) {
        FetchSearchVideoDetailTask task = new FetchSearchVideoDetailTask(categoryMap, mCategoryMapDAO, mVideoDetailDAO, mService, mVideoDetailPageParser, query);
        mAppExecutors.networkIO().execute(task);
    }

}
