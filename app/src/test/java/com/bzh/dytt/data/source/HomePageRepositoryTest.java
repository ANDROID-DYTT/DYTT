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
    private DyttService mDyttService;

    @Before
    public void setupItemRepository() throws IOException {
        MockitoAnnotations.initMocks(this);

        mHomePageRepository = new HomePageRepository();
        mHomePageRepository.setService(mDyttService);
        mHomePageRepository.setDao(mHomeItemLocalDao);
    }

    @After
    public void destroyItemRepository() {
        mHomePageRepository = null;
    }

    @Test
    public void getItems_requestHomeNewestFromDB() {
        mHomePageRepository.getItems(HomeType.NEWEST);
        verify(mHomeItemLocalDao, times(1)).getItemsByType(HomeType.NEWEST);
    }

//    @Test
//    public void getItems_requestHomeNewestFromDB() {
//
//        MutableLiveData<List<HomeItem>> itemsByType = new MediatorLiveData<>();
//
//        when(mHomeItemLocalDao.getItemsByType(HomeType.NEWEST)).thenReturn(itemsByType);
//
//        LiveData<Resource<List<HomeItem>>> items = mHomePageRepository.getItems(HomeType.NEWEST);
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
