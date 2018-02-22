package com.bzh.dytt.data.source;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bzh.dytt.data.HomeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HomePageRepositoryTest {


    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private HomePageRepository mHomePageRepository;

    @Mock
    private HomeItemDao mHomeItemLocalDao;

    @Mock
    private HomeAreaDao mHomeAreaDao;

    @Mock
    private DyttService mDyttService;

    @Before
    public void setupItemRepository() throws IOException {
        MockitoAnnotations.initMocks(this);

        mHomePageRepository = new HomePageRepository();
        mHomePageRepository.setService(mDyttService);
        mHomePageRepository.setHomeItemDao(mHomeItemLocalDao);
        mHomePageRepository.setHomeAreaDao(mHomeAreaDao);
    }

    @After
    public void destroyItemRepository() {
        mHomePageRepository = null;
    }

    @Test
    public void getHomeItems() {
        mHomePageRepository.getHomeItems(HomeType.NEWEST);
        verify(mHomeItemLocalDao, times(1)).getItemsByType(HomeType.NEWEST);
    }

    @Test
    public void getHomeAreas() throws Exception {
        mHomePageRepository.getHomeAreas();
        verify(mHomeAreaDao, times(1)).getAreas();
    }

//    @Test
//    public void getItems_requestHomeNewestFromDB() {
//
//        MutableLiveData<List<HomeItem>> itemsByType = new MediatorLiveData<>();
//
//        when(mHomeItemLocalDao.getItemsByType(HomeType.NEWEST)).thenReturn(itemsByType);
//
//        LiveData<Resource<List<HomeItem>>> items = mHomePageRepository.getHomeItems(HomeType.NEWEST);
//
//        items.observeForever(new Observer<Resource<List<HomeItem>>>() {
//            @Override
//            public void onChanged(@Nullable Resource<List<HomeItem>> listResource) {
//                System.out.println("Update UI");
//            }
//        });
//
//        itemsByType.setValue(new ArrayList<HomeItem>());
//
//        // Then items are loaded from the db
//        verify(mHomeItemLocalDao, times(1)).getItemsByType(HomeType.NEWEST);
//    }
}
