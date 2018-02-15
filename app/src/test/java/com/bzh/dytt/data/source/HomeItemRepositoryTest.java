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

public class HomeItemRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private HomeItemRepository mHomeItemRepository;

    @Mock
    private HomeItemDao mHomeItemLocalDao;

    @Mock
    private DyttService mDyttService;

    @Before
    public void setupItemRepository() throws IOException {
        MockitoAnnotations.initMocks(this);

        mHomeItemRepository = new HomeItemRepository();
        mHomeItemRepository.setService(mDyttService);
        mHomeItemRepository.setDao(mHomeItemLocalDao);
    }

    @After
    public void destroyItemRepository() {
        mHomeItemRepository = null;
    }

    @Test
    public void getItems_requestHomeNewestFromDB() {
        mHomeItemRepository.getItems(HomeType.NEWEST);
        verify(mHomeItemLocalDao, times(1)).getItemsByType(HomeType.NEWEST);
    }

//    @Test
//    public void getItems_requestHomeNewestFromDB() {
//
//        MutableLiveData<List<HomeItem>> itemsByType = new MediatorLiveData<>();
//
//        when(mHomeItemLocalDao.getItemsByType(HomeType.NEWEST)).thenReturn(itemsByType);
//
//        LiveData<Resource<List<HomeItem>>> items = mHomeItemRepository.getItems(HomeType.NEWEST);
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
