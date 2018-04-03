package com.bzh.dytt;

import com.bzh.dytt.data.db.AppDatabase;
import com.bzh.dytt.data.db.CategoryMapDAO;
import com.bzh.dytt.data.db.CategoryPageDAO;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.util.HomePageParser;
import com.bzh.dytt.util.InstantAppExecutors;
import com.bzh.dytt.util.LoadableMovieParser;
import com.bzh.dytt.util.VideoDetailPageParser;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DataRepositoryTest {

    private DataRepository dataRepository;

    @Before
    public void init() throws Exception {

        DyttService dyttService = mock(DyttService.class);
        AppDatabase appDatabase = mock(AppDatabase.class);
        CategoryMapDAO categoryMapDAO = mock(CategoryMapDAO.class);
        CategoryPageDAO categoryPageDAO = mock(CategoryPageDAO.class);
        VideoDetailDAO videoDetailDAO = mock(VideoDetailDAO.class);

        when(appDatabase.categoryMapDAO()).thenReturn(categoryMapDAO);
        when(appDatabase.categoryPageDAO()).thenReturn(categoryPageDAO);
        when(appDatabase.videoDetailDAO()).thenReturn(videoDetailDAO);

        HomePageParser homePageParser = new HomePageParser();
        VideoDetailPageParser videoDetailPageParser = new VideoDetailPageParser();
        LoadableMovieParser loadableMovieParser = new LoadableMovieParser();

        dataRepository = new DataRepository(new InstantAppExecutors(), dyttService, categoryMapDAO, categoryPageDAO, videoDetailDAO, homePageParser, videoDetailPageParser, loadableMovieParser);
    }

}