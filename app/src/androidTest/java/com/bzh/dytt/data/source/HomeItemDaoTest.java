package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class HomeItemDaoTest {

    private HomeItem mHomeItem = new HomeItem("三块广告牌 BD中英双字幕", "2018-02-14", "http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html", HomeType.NEWEST);

    private AppDatabase mDatabase;

    @Before
    public void setUp() throws Exception {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase.class).build();
    }

    @After
    public void tearDown() throws Exception {
//        mDatabase.close();
//        mDatabase = null;
    }

    @Test
    public void insertHomeItem() {
        LiveData<HomeItem> item = mDatabase.homeItemDAO().getItemById("http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html".hashCode());

        item.observeForever(new Observer<HomeItem>() {
            @Override
            public void onChanged(@Nullable HomeItem homeItem) {
                assertThat(homeItem, notNullValue());
                assertThat(homeItem.getTitle(), is("三块广告牌 BD中英双字幕"));
                assertThat(homeItem.getTime(), is("2018-02-14"));
                assertThat(homeItem.getType(), is(HomeType.NEWEST));
            }
        });

        mDatabase.homeItemDAO().insertItem(mHomeItem);
    }

    @Test
    public void insertSameIdHomeItem() {

        mDatabase.homeItemDAO().insertItem(mHomeItem);

        LiveData<HomeItem> item = mDatabase.homeItemDAO().getItemById("http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html".hashCode());

        item.observeForever(new Observer<HomeItem>() {
            @Override
            public void onChanged(@Nullable HomeItem homeItem) {
                assertThat(homeItem, notNullValue());
                assertThat(homeItem.getTitle(), is("最美的中国"));
            }
        });

        mHomeItem.setTitle("最美的中国");
        mDatabase.homeItemDAO().insertItem(mHomeItem);
    }


    @Test
    public void getAllItem() {
        LiveData<List<HomeItem>> items = mDatabase.homeItemDAO().getItems();

        items.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(1));
            }
        });
        mDatabase.homeItemDAO().insertItem(mHomeItem);
    }

    @Test
    public void updateItem() {

        mDatabase.homeItemDAO().insertItem(mHomeItem);

        LiveData<List<HomeItem>> item = mDatabase.homeItemDAO().getItems();

        item.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(1));
                assertThat(homeItems.get(0).getId(), is("http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html".hashCode()));
                assertThat(homeItems.get(0).getTitle(), is("《伯德小姐》HD中英双字幕"));
            }
        });

        HomeItem mHomeItem = new HomeItem("《伯德小姐》HD中英双字幕", "2018-02-13", "http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html", HomeType.NEWEST);
        mDatabase.homeItemDAO().updateItem(mHomeItem);
    }

    @Test
    public void getItemById() {

        mDatabase.homeItemDAO().insertItem(mHomeItem);

        LiveData<HomeItem> item = mDatabase.homeItemDAO().getItemById(mHomeItem.getId());

        item.observeForever(new Observer<HomeItem>() {
            @Override
            public void onChanged(@Nullable HomeItem homeItem) {
                assertThat(homeItem, notNullValue());
                assertThat(homeItem.getTitle(), is("三块广告牌 BD中英双字幕"));
            }
        });
    }

    @Test
    public void getItemsByType() {
        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 4", "2-14", "http://4", HomeType.NEWEST));
        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 3", "2-14", "http://3", HomeType.NEWEST));
        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 2", "2-14", "http://2", HomeType.NEWEST));
        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 1", "2-14", "http://1", HomeType.NEWEST));

        LiveData<List<HomeItem>> newests = mDatabase.homeItemDAO().getItemsByType(HomeType.NEWEST);

        newests.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(4));
            }
        });

        LiveData<List<HomeItem>> films = mDatabase.homeItemDAO().getItemsByType(HomeType.FILM);

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

        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 4", "2-14", "http://4", HomeType.NEWEST));
        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 3", "2-14", "http://3", HomeType.NEWEST));
        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 2", "2-14", "http://2", HomeType.NEWEST));
        mDatabase.homeItemDAO().insertItem(new HomeItem("newest - 1", "2-14", "http://1", HomeType.NEWEST));

        mDatabase.homeItemDAO().deleteItems();

        LiveData<List<HomeItem>> all = mDatabase.homeItemDAO().getItems();

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

        mDatabase.homeItemDAO().insertItem(mHomeItem);

        mDatabase.homeItemDAO().deleteItemById(mHomeItem.getId());

        LiveData<List<HomeItem>> all = mDatabase.homeItemDAO().getItems();

        all.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(0));
            }
        });
    }

    @Test
    public void deleteItem() {

        mDatabase.homeItemDAO().insertItem(mHomeItem);

        mDatabase.homeItemDAO().deleteItem(mHomeItem);

        LiveData<List<HomeItem>> all = mDatabase.homeItemDAO().getItems();

        all.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(0));
            }
        });
    }

    @Test
    public void insertItems() {
        List<HomeItem> homeItems = new ArrayList<>();
        homeItems.add(new HomeItem("newest - 4", "2-14", "http://4", HomeType.NEWEST));
        homeItems.add(new HomeItem("newest - 3", "2-14", "http://3", HomeType.NEWEST));
        homeItems.add(new HomeItem("newest - 2", "2-14", "http://2", HomeType.NEWEST));
        homeItems.add(new HomeItem("newest - 1", "2-14", "http://1", HomeType.NEWEST));
        mDatabase.homeItemDAO().insertItems(homeItems);

        LiveData<List<HomeItem>> all = mDatabase.homeItemDAO().getItems();

        all.observeForever(new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> homeItems) {
                assertThat(homeItems, notNullValue());
                assertThat(homeItems.size(), is(homeItems.size()));
            }
        });
    }
}
