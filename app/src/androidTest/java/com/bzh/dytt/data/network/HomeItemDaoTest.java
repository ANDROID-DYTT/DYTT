package com.bzh.dytt.data.network;

import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.LiveDataTestUtil;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;
import com.bzh.dytt.data.db.AppDatabaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class HomeItemDaoTest extends AppDatabaseTest {

    private HomeItem mHomeItem = new HomeItem("三块广告牌 BD中英双字幕", "2018-02-14", "http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html", HomeType.NEWEST);

    @Test
    public void insertHomeItem() throws InterruptedException {
        mDB.homeItemDAO().insertItem(mHomeItem);

        HomeItem homeItem = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemById("http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html".hashCode()));
        assertThat(homeItem, notNullValue());
        assertThat(homeItem.getTitle(), is("三块广告牌 BD中英双字幕"));
        assertThat(homeItem.getTime(), is("2018-02-14"));
        assertThat(homeItem.getType(), is(HomeType.NEWEST));
    }

    @Test
    public void insertSameIdHomeItem() throws InterruptedException {

        mDB.homeItemDAO().insertItem(mHomeItem);
        mHomeItem.setTitle("最美的中国");
        mDB.homeItemDAO().insertItem(mHomeItem);

        HomeItem homeItem = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemById("http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html".hashCode()));

        assertThat(homeItem, notNullValue());
        assertThat(homeItem.getTitle(), is("最美的中国"));

    }


    @Test
    public void getAllItem() throws InterruptedException {
        mDB.homeItemDAO().insertItem(mHomeItem);
        List<HomeItem> homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());

        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(1));
    }

    @Test
    public void updateItem() throws InterruptedException {

        mDB.homeItemDAO().insertItem(mHomeItem);
        HomeItem mHomeItem = new HomeItem("《伯德小姐》HD中英双字幕", "2018-02-13", "http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html", HomeType.NEWEST);
        mDB.homeItemDAO().updateItem(mHomeItem);
        List<HomeItem> homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());

        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(1));
        assertThat(homeItems.get(0).getId(), is("http://www.dytt8.net/html/gndy/dyzz/20180214/56321.html".hashCode()));
        assertThat(homeItems.get(0).getTitle(), is("《伯德小姐》HD中英双字幕"));
    }


    @Test
    public void getItemById() throws InterruptedException {

        mDB.homeItemDAO().insertItem(mHomeItem);

        HomeItem homeItem = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemById(mHomeItem.getId()));
        assertThat(homeItem, notNullValue());
        assertThat(homeItem.getTitle(), is("三块广告牌 BD中英双字幕"));
    }

    @Test
    public void getItemsByType() throws InterruptedException {
        mDB.homeItemDAO().insertItem(new HomeItem("newest - 4", "2-14", "http://4", HomeType.NEWEST));
        mDB.homeItemDAO().insertItem(new HomeItem("newest - 3", "2-14", "http://3", HomeType.NEWEST));
        mDB.homeItemDAO().insertItem(new HomeItem("newest - 2", "2-14", "http://2", HomeType.NEWEST));
        mDB.homeItemDAO().insertItem(new HomeItem("newest - 1", "2-14", "http://1", HomeType.NEWEST));

        List<HomeItem> homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.NEWEST));
        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(4));

        homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.FILM));
        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(0));
    }

    @Test
    public void deleteHomeItems() throws InterruptedException {

        mDB.homeItemDAO().insertItem(new HomeItem("newest - 4", "2-14", "http://4", HomeType.NEWEST));
        mDB.homeItemDAO().insertItem(new HomeItem("newest - 3", "2-14", "http://3", HomeType.NEWEST));
        mDB.homeItemDAO().insertItem(new HomeItem("newest - 2", "2-14", "http://2", HomeType.NEWEST));
        mDB.homeItemDAO().insertItem(new HomeItem("newest - 1", "2-14", "http://1", HomeType.NEWEST));

        mDB.homeItemDAO().deleteItems();

        List<HomeItem> homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());

        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(0));
    }

    @Test
    public void deleteItemById() throws InterruptedException {

        mDB.homeItemDAO().insertItem(mHomeItem);

        mDB.homeItemDAO().deleteItemById(mHomeItem.getId());

        List<HomeItem> homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());

        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(0));
    }

    @Test
    public void deleteItem() throws InterruptedException {

        mDB.homeItemDAO().insertItem(mHomeItem);

        mDB.homeItemDAO().deleteItem(mHomeItem);

        List<HomeItem> homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());

        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(0));
    }

    @Test
    public void insertItems() throws InterruptedException {
        List<HomeItem> homeItems = new ArrayList<>();
        homeItems.add(new HomeItem("newest - 4", "2-14", "http://4", HomeType.NEWEST));
        homeItems.add(new HomeItem("newest - 3", "2-14", "http://3", HomeType.NEWEST));
        homeItems.add(new HomeItem("newest - 2", "2-14", "http://2", HomeType.NEWEST));
        homeItems.add(new HomeItem("newest - 1", "2-14", "http://1", HomeType.NEWEST));
        mDB.homeItemDAO().insertItems(homeItems);

        List<HomeItem> items = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());

        assertThat(items, notNullValue());
        assertThat(items.size(), is((homeItems.size())));
    }
}
