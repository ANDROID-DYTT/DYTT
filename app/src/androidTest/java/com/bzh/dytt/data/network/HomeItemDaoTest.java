package com.bzh.dytt.data.network;

import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.LiveDataTestUtil;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;
import com.bzh.dytt.data.db.AppDatabaseTest;
import com.bzh.dytt.util.AssetsUtil;
import com.bzh.dytt.util.HomePageParser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class HomeItemDaoTest extends AppDatabaseTest {

    @Test
    public void getItemsByTypeOrderByDesc() throws InterruptedException, IOException {
        insertAllItems();

        List<HomeItem> newest = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.NEWEST));
        assertTrue(newest.size() > 0);

        List<HomeItem> newest168 = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.NEWEST_168));
        assertTrue(newest168.size() > 0);

        List<HomeItem> thunder = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.THUNDER));
        assertTrue(thunder.size() > 0);

        List<HomeItem> chinaTV = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.CHINA_TV));
        assertTrue(chinaTV.size() > 0);

        List<HomeItem> eaTv = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.EA_TV));
        assertTrue(eaTv.size() > 0);

        List<HomeItem> jskTV = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItemsByType(HomeType.JSK_TV));
        assertTrue(jskTV.size() > 0);
    }


    @Test
    public void deleteHomeItems() throws InterruptedException, IOException {
        mDB.homeItemDAO().deleteItems();
        List<HomeItem> homeItems = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());
        assertThat(homeItems, notNullValue());
        assertThat(homeItems.size(), is(0));
    }

    @Test
    public void insertNewestItems() throws InterruptedException, IOException {

        String homePage = AssetsUtil.getAssest("index.html");
        HomePageParser.NewestParse newestParse = new HomePageParser.NewestParse();
        List<HomeItem> homeItems = newestParse.parseItems(homePage);
        mDB.homeItemDAO().insertItems(homeItems);

        List<HomeItem> items = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());
        assertThat(items, notNullValue());
        assertThat(items.size(), is((homeItems.size())));
    }

    @Test
    public void insertAllItems() throws IOException, InterruptedException {
        String homePage = AssetsUtil.getAssest("index.html");
        HomePageParser homePageParser = new HomePageParser();
        List<HomeItem> homeItems = homePageParser.parseItems(homePage);
        mDB.homeItemDAO().insertItems(homeItems);

        List<HomeItem> items = LiveDataTestUtil.getValue(mDB.homeItemDAO().getItems());
        assertThat(items, notNullValue());
        assertThat(items.size(), is((homeItems.size())));
    }
}
