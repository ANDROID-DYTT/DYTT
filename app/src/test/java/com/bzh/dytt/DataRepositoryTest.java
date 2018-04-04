package com.bzh.dytt;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.CategoryPage;
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
import com.bzh.dytt.util.CategoryPageParser;
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
import retrofit2.Response;

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
    private CategoryPageParser categoryPageParser;
    private VideoDetailPageParser videoDetailPageParser;

    @Before
    public void init() {

        dyttService = mock(DyttService.class);
        AppDatabase appDatabase = mock(AppDatabase.class);
        categoryMapDAO = mock(CategoryMapDAO.class);
        categoryPageDAO = mock(CategoryPageDAO.class);
        videoDetailDAO = mock(VideoDetailDAO.class);

        when(appDatabase.categoryMapDAO()).thenReturn(categoryMapDAO);
        when(appDatabase.categoryPageDAO()).thenReturn(categoryPageDAO);
        when(appDatabase.videoDetailDAO()).thenReturn(videoDetailDAO);
        videoDetailPageParser = new VideoDetailPageParser();
        categoryPageParser = new CategoryPageParser(new HomePageParser(), new LoadableMovieParser());
        dataRepository = new DataRepository(new InstantAppExecutors(), dyttService, categoryMapDAO, categoryPageDAO, videoDetailDAO, categoryPageParser, videoDetailPageParser);
    }

    @Test
    public void getMovieListByCategory_Home_Latest_Movie() throws IOException {
        MovieCategory movieCategory = MovieCategory.HOME_LATEST_MOVIE;

        // Support DB
        MutableLiveData<List<CategoryMap>> dbLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategory(movieCategory)).thenReturn(dbLiveData);

        // Support Success API
        String resource = TestUtils.getResource(getClass(), "home.html");
        LiveData<ApiResponse<ResponseBody>> call = ApiUtil.successCall(ResponseBody.create(MediaType.parse("text/html"), resource));
        when(dyttService.getMovieListByCategory(movieCategory.getDefaultUrl())).thenReturn(call);

        // Support Category Maps
        List<CategoryMap> categoryMaps = categoryPageParser.parse(resource, movieCategory);

        // Support Video Details
        List<VideoDetail> videoDetails = new ArrayList<>();
        for (CategoryMap category : categoryMaps) {
            VideoDetail videoDetail = new VideoDetail();
            videoDetails.add(videoDetail.updateValue(category));
        }

        // Init
        LiveData<Resource<List<CategoryMap>>> latestMovie = dataRepository.getMovieListByCategory(movieCategory);
        Observer<Resource<List<CategoryMap>>> observer = mock(Observer.class);
        latestMovie.observeForever(observer);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));

        // Post Update Result
        MutableLiveData<List<CategoryMap>> updateDBLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategory(movieCategory)).thenReturn(updateDBLiveData);

        // Start Verify
        dbLiveData.postValue(null);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));
        verify(categoryMapDAO).insertCategoryMapList(categoryMaps);
        verify(videoDetailDAO).insertVideoDetailList(videoDetails);
        verify(categoryPageDAO).insertPage(movieCategory.getDefaultPage());

        // Verify Result
        updateDBLiveData.postValue(categoryMaps);
        verify(observer).onChanged(Resource.success(categoryMaps));
    }

    @Test
    public void getMovieListByCategory_New_Movie() throws IOException {
        MovieCategory movieCategory = MovieCategory.NEW_MOVIE;

        // Support DB
        MutableLiveData<List<CategoryMap>> dbLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategory(movieCategory)).thenReturn(dbLiveData);

        // Support Success API
        String resource = TestUtils.getResource(getClass(), "new_movie.html");
        LiveData<ApiResponse<ResponseBody>> call = ApiUtil.successCall(ResponseBody.create(MediaType.parse("text/html"), resource));
        when(dyttService.getMovieListByCategory(movieCategory.getDefaultUrl())).thenReturn(call);

        // Support Category Maps
        List<CategoryMap> categoryMaps = categoryPageParser.parse(resource, movieCategory);

        // Support Video Details
        List<VideoDetail> videoDetails = new ArrayList<>();
        for (CategoryMap category : categoryMaps) {
            VideoDetail videoDetail = new VideoDetail();
            videoDetails.add(videoDetail.updateValue(category));
        }

        // Init
        LiveData<Resource<List<CategoryMap>>> latestMovie = dataRepository.getMovieListByCategory(movieCategory);
        Observer<Resource<List<CategoryMap>>> observer = mock(Observer.class);
        latestMovie.observeForever(observer);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));

        // Post Update Result
        MutableLiveData<List<CategoryMap>> updateDBLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategory(movieCategory)).thenReturn(updateDBLiveData);

        // Start Verify
        dbLiveData.postValue(null);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));
        verify(categoryMapDAO).insertCategoryMapList(categoryMaps);
        verify(videoDetailDAO).insertVideoDetailList(videoDetails);
        verify(categoryPageDAO).insertPage(movieCategory.getDefaultPage());

        // Verify Result
        updateDBLiveData.postValue(categoryMaps);
        verify(observer).onChanged(Resource.success(categoryMaps));
    }

    @Test
    public void search() throws IOException {
        MovieCategory movieCategory = MovieCategory.SEARCH_MOVIE;
        String query = "魅影缝匠";

        // DB
        MutableLiveData<List<CategoryMap>> dbLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategoryAndQuery(movieCategory, query)).thenReturn(dbLiveData);

        // NET
        String resource = TestUtils.getResource(getClass(), "query.html");
        LiveData<ApiResponse<ResponseBody>> call = ApiUtil.successCall(ResponseBody.create(MediaType.parse("text/html"), resource));
        String url = String.format("http://s.ygdy8.com/plus/so.php?kwtype=0&searchtype=title&keyword=%s", query);
        when(dyttService.search(url)).thenReturn(call);

        // Support Category Maps
        List<CategoryMap> categoryMaps = categoryPageParser.parse(resource, movieCategory);
        // Support Video Details
        List<VideoDetail> videoDetails = new ArrayList<>();
        for (CategoryMap category : categoryMaps) {
            VideoDetail videoDetail = new VideoDetail();
            videoDetails.add(videoDetail.updateValue(category));
        }

        // Init
        LiveData<Resource<List<CategoryMap>>> searchLiveData = dataRepository.search(movieCategory, query);
        Observer<Resource<List<CategoryMap>>> observer = mock(Observer.class);
        searchLiveData.observeForever(observer);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));

        // Update Result
        MutableLiveData<List<CategoryMap>> updateDBLiveData = new MutableLiveData<>();
        when(categoryMapDAO.getMovieLinksByCategoryAndQuery(movieCategory, query)).thenReturn(updateDBLiveData);

        // Start Verify
        dbLiveData.postValue(null);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));
        verify(categoryMapDAO).insertCategoryMapList(categoryMaps);
        verify(videoDetailDAO).insertVideoDetailList(videoDetails);

        // Verify Result
        updateDBLiveData.postValue(categoryMaps);
        verify(observer).onChanged(Resource.success(categoryMaps));
    }

    @Test
    public void getNextMovieListByCategory() throws IOException {
        MovieCategory newMovie = MovieCategory.NEW_MOVIE;

        // Support Next Page
        CategoryPage nextPage = new CategoryPage(newMovie, 1);
        when(categoryPageDAO.nextPage(newMovie)).thenReturn(nextPage);

        // Support Call
        MutableLiveData<ApiResponse<ResponseBody>> apiResponseMutableLiveData = new MutableLiveData<>();
        String resource = TestUtils.getResource(getClass(), "next_new_movie.html");
        ApiResponse<ResponseBody> apiResponse = new ApiResponse<ResponseBody>(Response.success(ResponseBody.create(MediaType.parse("text/html"), resource)));
        when(dyttService.getMovieListByCategory("/html/gndy/dyzz/list_23_2.html")).thenReturn(apiResponseMutableLiveData);

        // MAPS
        List<CategoryMap> categoryMaps = categoryPageParser.parse(resource, newMovie);
        List<VideoDetail> details = new ArrayList<>();
        for (CategoryMap category : categoryMaps) {
            VideoDetail videoDetail = new VideoDetail();
            videoDetail.updateValue(category);
            details.add(videoDetail);
        }

        // Init
        LiveData<Resource<List<CategoryMap>>> movieList = dataRepository.getNextMovieListByCategory(newMovie);
        Observer<Resource<List<CategoryMap>>> observer = mock(Observer.class);
        movieList.observeForever(observer);
        verify(observer).onChanged(Resource.<List<CategoryMap>>loading(null));

        // Verify
        verify(categoryPageDAO).nextPage(newMovie);
        apiResponseMutableLiveData.setValue(apiResponse);
        verify(categoryMapDAO).insertCategoryMapList(categoryMaps);
        verify(videoDetailDAO).insertVideoDetailList(details);
        verify(categoryPageDAO).updatePage(nextPage);
    }
}