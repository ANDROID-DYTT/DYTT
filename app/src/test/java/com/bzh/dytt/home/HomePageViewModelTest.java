package com.bzh.dytt.home;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.content.res.Resources;

import com.bzh.dytt.DataRepository;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;
import com.bzh.dytt.data.network.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomePageViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule mInstantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Application mContext;

    @Mock
    private DataRepository mRepository;

    private HomePageViewModel mHomePageViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mContext.getApplicationContext()).thenReturn(mContext);
        when(mContext.getResources()).thenReturn(mock(Resources.class));

        mHomePageViewModel = new HomePageViewModel(mRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHomeArea() throws Exception {
        LiveData<Resource<List<HomeArea>>> homeArea = mHomePageViewModel.getHomeArea();

        verify(mRepository).getHomeAreas();

    }

    @Test
    public void getHomeItems() throws Exception {

        LiveData<Resource<List<HomeItem>>> homeItems = mHomePageViewModel.getHomeItems(HomeType.NEWEST_168);

        verify(mRepository).getHomeItems(HomeType.NEWEST_168);
    }

}