package com.bzh.dytt.home;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class NewMovieViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    private NewMovieViewModel viewModel;
    private DataRepository dataRepository;

    @Before
    public void setUp() {
        dataRepository = mock(DataRepository.class);
    }

    @Test
    public void construct() {
        // repository
        MutableLiveData<Resource<List<CategoryMap>>> dbCategoryLiveData = new MutableLiveData<>();
        when(dataRepository.getMovieListByCategory(MovieCategory.HOME_LATEST_MOVIE)).thenReturn(dbCategoryLiveData);

        MutableLiveData<Resource<List<VideoDetail>>> dbVideoDetailLiveData = new MutableLiveData<>();
        when(dataRepository.getVideoDetailsByCategory(MovieCategory.HOME_LATEST_MOVIE)).thenReturn(dbVideoDetailLiveData);

        // init
        viewModel = new NewMovieViewModel(dataRepository);
        Observer<Resource<List<VideoDetail>>> observer = mock(Observer.class);
        viewModel.getNewMovieList().observeForever(observer);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void refresh() {
        // repository
        MutableLiveData<Resource<List<CategoryMap>>> dbCategoryLiveData = new MutableLiveData<>();
        when(dataRepository.getMovieListByCategory(MovieCategory.HOME_LATEST_MOVIE)).thenReturn(dbCategoryLiveData);

        MutableLiveData<Resource<List<VideoDetail>>> dbVideoDetailLiveData = new MutableLiveData<>();
        when(dataRepository.getVideoDetailsByCategory(MovieCategory.HOME_LATEST_MOVIE)).thenReturn(dbVideoDetailLiveData);

        // init
        viewModel = new NewMovieViewModel(dataRepository);
        Observer<Resource<List<VideoDetail>>> observer = mock(Observer.class);
        viewModel.getNewMovieList().observeForever(observer);

        viewModel.refresh();

        // verify
        verify(dataRepository).getMovieListByCategory(MovieCategory.HOME_LATEST_MOVIE);
        dbCategoryLiveData.setValue(Resource.<List<CategoryMap>>loading(null));
        List<CategoryMap> categoryMaps = new ArrayList<>();
        dbCategoryLiveData.setValue(Resource.success(categoryMaps));

        verify(dataRepository).getVideoDetailsByCategory(MovieCategory.HOME_LATEST_MOVIE);
        List<VideoDetail> videoDetails = new ArrayList<>();
        dbVideoDetailLiveData.setValue(Resource.success(videoDetails));
        verify(observer).onChanged(Resource.<List<VideoDetail>>success(videoDetails));
    }
}