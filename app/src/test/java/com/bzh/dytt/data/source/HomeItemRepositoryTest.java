package com.bzh.dytt.data.source;

import android.arch.lifecycle.MediatorLiveData;

import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeItemType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verify;

public class HomeItemRepositoryTest {

    private HomeItemRepository mHomeItemRepository;

    @Mock
    private DyttService mDyttService;

    @Mock
    private HomeItemDao mHomeItemLocalDao;

    @Mock
    private MediatorLiveData<Resource<List<HomeItem>>> mHomeItemsLiveData;

    @Before
    public void setupItemRepository() {
        MockitoAnnotations.initMocks(this);

        mHomeItemRepository = new HomeItemRepository();
        mHomeItemRepository.setService(mDyttService);
        mHomeItemRepository.setDao(mHomeItemLocalDao);
        mHomeItemRepository.setMediatorLiveData(mHomeItemsLiveData);
    }

    @After
    public void destroyItemRepository() {
        mHomeItemRepository = null;
    }

    @Test
    public void getItems_requestHomeNewestFromDB() {

        // When items are requested from the items repository
        mHomeItemRepository.getItems(HomeItemType.NEWEST);

        // Then items are loaded from the db
        verify(mHomeItemLocalDao).getItemsByType(HomeItemType.NEWEST);
    }

}
