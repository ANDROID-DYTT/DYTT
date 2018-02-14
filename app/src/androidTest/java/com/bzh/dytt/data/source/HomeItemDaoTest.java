package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeItemType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class HomeItemDaoTest {

    private HomeItem mHomeItem = new HomeItem("三块广告牌 BD中英双字幕", "2018-02-14", "http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html", HomeItemType.NEWEST);

    private MyDatabase mDatabase;

    @Before
    public void setUp() throws Exception {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), MyDatabase.class).build();
    }

    @After
    public void tearDown() throws Exception {
//        mDatabase.close();
//        mDatabase = null;
    }

    @Test
    public void insertHomeItem() {
        LiveData<HomeItem> item = mDatabase.itemHomeDao().getItemById(1);

        item.observeForever(new Observer<HomeItem>() {
            @Override
            public void onChanged(@Nullable HomeItem homeItem) {
                assertThat(homeItem, notNullValue());
                assertThat(homeItem.getName(), is("三块广告牌 BD中英双字幕"));
                assertThat(homeItem.getTime(), is("2018-02-14"));
                assertThat(homeItem.getType(), is(HomeItemType.NEWEST));
            }
        });
        mDatabase.itemHomeDao().insertItem(mHomeItem);
    }

    @Test
    public void getAllItem() {
        LiveData<List<HomeItem>> items = mDatabase.itemHomeDao().getItems();

        items.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(1));
            }
        });
        mDatabase.itemHomeDao().insertItem(mHomeItem);
    }

    @Test
    public void updateItem() {

        mDatabase.itemHomeDao().insertItem(mHomeItem);

        LiveData<HomeItem> item = mDatabase.itemHomeDao().getItemById(1);

        item.observeForever(new Observer<HomeItem>() {
            @Override
            public void onChanged(@Nullable HomeItem homeItem) {
                assertThat(homeItem, notNullValue());
                assertThat(homeItem.getName(), is("《伯德小姐》HD中英双字幕"));
            }
        });

        HomeItem mHomeItem = new HomeItem(1, "《伯德小姐》HD中英双字幕", "2018-02-13", "http://www.dytt8.net/html/gndy/dyzz/20180213/56320.html", HomeItemType.NEWEST);
        mDatabase.itemHomeDao().updateItem(mHomeItem);
    }

    @Test
    public void getItemById() {

        mDatabase.itemHomeDao().insertItem(mHomeItem);

        LiveData<HomeItem> item = mDatabase.itemHomeDao().getItemById(1);

        item.observeForever(new Observer<HomeItem>() {
            @Override
            public void onChanged(@Nullable HomeItem homeItem) {
                assertThat(homeItem, notNullValue());
                assertThat(homeItem.getName(), is("三块广告牌 BD中英双字幕"));
            }
        });
    }

    @Test
    public void getItemsByType() {
        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 4", "2-14", "http://", HomeItemType.NEWEST));
        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 3", "2-14", "http://", HomeItemType.NEWEST));
        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 2", "2-14", "http://", HomeItemType.NEWEST));
        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 1", "2-14", "http://", HomeItemType.NEWEST));

        LiveData<List<HomeItem>> newests = mDatabase.itemHomeDao().getItemsByType(HomeItemType.NEWEST);

        newests.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(4));
            }
        });

        LiveData<List<HomeItem>> films = mDatabase.itemHomeDao().getItemsByType(HomeItemType.FILM);

        films.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(0));
            }
        });
    }

    @Test
    public void deleteHomeItems() {

        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 4", "2-14", "http://", HomeItemType.NEWEST));
        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 3", "2-14", "http://", HomeItemType.NEWEST));
        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 2", "2-14", "http://", HomeItemType.NEWEST));
        mDatabase.itemHomeDao().insertItem(new HomeItem("newest - 1", "2-14", "http://", HomeItemType.NEWEST));

        mDatabase.itemHomeDao().deleteItems();

        LiveData<List<HomeItem>> all = mDatabase.itemHomeDao().getItems();

        all.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(0));
            }
        });
    }

    @Test
    public void deleteItemById() {

        mDatabase.itemHomeDao().insertItem(mHomeItem);

        mDatabase.itemHomeDao().deleteItemById(1);

        LiveData<List<HomeItem>> all = mDatabase.itemHomeDao().getItems();

        all.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(0));
            }
        });
    }
}