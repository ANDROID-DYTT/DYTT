package com.bzh.dytt;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.AppDatabase;
import com.bzh.dytt.data.db.CategoryMapDAO;
import com.bzh.dytt.data.db.CategoryPageDAO;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.network.ApiResponse;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.util.ApiUtil;
import com.bzh.dytt.util.HomePageParser;
import com.bzh.dytt.util.InstantAppExecutors;
import com.bzh.dytt.util.LoadableMovieParser;
import com.bzh.dytt.util.VideoDetailPageParser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DataRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DataRepository dataRepository;
    private CategoryMapDAO categoryMapDAO;
    private CategoryPageDAO categoryPageDAO;
    private VideoDetailDAO videoDetailDAO;
    private DyttService dyttService;
    private HomePageParser homePageParser;
    private VideoDetailPageParser videoDetailPageParser;
    private LoadableMovieParser loadableMovieParser;

    @Before
    public void init() throws Exception {

        dyttService = mock(DyttService.class);
        AppDatabase appDatabase = mock(AppDatabase.class);
        categoryMapDAO = mock(CategoryMapDAO.class);
        categoryPageDAO = mock(CategoryPageDAO.class);
        videoDetailDAO = mock(VideoDetailDAO.class);

        when(appDatabase.categoryMapDAO()).thenReturn(categoryMapDAO);
        when(appDatabase.categoryPageDAO()).thenReturn(categoryPageDAO);
        when(appDatabase.videoDetailDAO()).thenReturn(videoDetailDAO);

        homePageParser = new HomePageParser();
        videoDetailPageParser = new VideoDetailPageParser();
        loadableMovieParser = new LoadableMovieParser();

        dataRepository = new DataRepository(new InstantAppExecutors(), dyttService, categoryMapDAO, categoryPageDAO, videoDetailDAO, homePageParser, videoDetailPageParser, loadableMovieParser);
    }

    @Test
    public void getLatestMovie() throws IOException {

        // Support DB
        MutableLiveData<List<CategoryMap>> dbLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategory(MovieCategory.HOME_LATEST_MOVIE)).thenReturn(dbLiveData);

        // Support Success API
        String resource = TestUtils.getResource(getClass(), "home.html");
        LiveData<ApiResponse<ResponseBody>> call = ApiUtil.successCall(ResponseBody.create(MediaType.parse("text/html"), resource));
        when(dyttService.getHomePage()).thenReturn(call);

        // Support Category Maps
        List<CategoryMap> categoryMaps = homePageParser.parseLatestMovieCategoryMap(resource);

        // Support Video Details
        List<VideoDetail> videoDetails = new ArrayList<>();
        for (CategoryMap category : categoryMaps) {
            VideoDetail videoDetail = new VideoDetail();
            videoDetail.setDetailLink(category.getLink());
            videoDetail.setSN(category.getSN());
            videoDetail.setCategory(category.getCategory());
            videoDetails.add(videoDetail);
        }

        // Init
        LiveData<Resource<List<CategoryMap>>> latestMovie = dataRepository.getLatestMovie();
        Observer<Resource<List<CategoryMap>>> observer = mock(Observer.class);
        latestMovie.observeForever(observer);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));

        // Post Update Result
        MutableLiveData<List<CategoryMap>> updateDBLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategory(MovieCategory.HOME_LATEST_MOVIE)).thenReturn(updateDBLiveData);

        // Start Verify
        dbLiveData.postValue(null);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));
        verify(categoryMapDAO).insertCategoryMapList(categoryMaps);
        verify(videoDetailDAO).insertVideoDetailList(videoDetails);

        // Verify Result
        updateDBLiveData.postValue(categoryMaps);
        verify(observer).onChanged(Resource.<List<CategoryMap>>success(categoryMaps));
    }
}