package com.bzh.dytt.data.network;

import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.LiveDataTestUtil;
import com.bzh.dytt.data.HomeArea;
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
public class HomeAreaDaoTest extends AppDatabaseTest {

    private HomeArea mArea = new HomeArea("2018新片精品", HomeType.NEWEST);

    @Test
    public void insertArea() throws Exception {

        mDB.homeAreaDAO().insertArea(mArea);

        HomeArea value = LiveDataTestUtil.getValue(mDB.homeAreaDAO().getAreaById(mArea.getId()));

        assertThat(value, notNullValue());
        assertThat(value.getId(), is(mArea.getId()));
        assertThat(value.getTitle(), is(mArea.getTitle()));
    }

    @Test
    public void insertAreas() throws Exception {

        List<HomeArea> areaList = new ArrayList<>();
        areaList.add(new HomeArea("最新电影", HomeType.NEWEST));
        areaList.add(new HomeArea("迅雷专区", HomeType.THUNDER));
        mDB.homeAreaDAO().insertAreas(areaList);

        List<HomeArea> homeAreas = LiveDataTestUtil.getValue(mDB.homeAreaDAO().getAreas());
        assertThat(homeAreas, notNullValue());
        assertThat(homeAreas.size(), is(2));
    }

    @Test
    public void updateArea() throws Exception {
        mDB.homeAreaDAO().insertArea(mArea);
        mArea.setType(HomeType.FILM);
        mDB.homeAreaDAO().updateArea(mArea);
        HomeArea homeArea = LiveDataTestUtil.getValue(mDB.homeAreaDAO().getAreaById(mArea.getId()));
        assertThat(homeArea, notNullValue());
        assertThat(homeArea.getType(), is(HomeType.FILM));
    }

    @Test
    public void deleteArea() throws Exception {
        mDB.homeAreaDAO().insertArea(mArea);
        mDB.homeAreaDAO().deleteArea(mArea);
        List<HomeArea> homeAreas = LiveDataTestUtil.getValue(mDB.homeAreaDAO().getAreas());
        assertThat(homeAreas, notNullValue());
        assertThat(homeAreas.size(), is(0));
    }

    @Test
    public void deleteAreas() throws Exception {
        List<HomeArea> areaList = new ArrayList<>();
        areaList.add(new HomeArea("最新电影", HomeType.NEWEST));
        areaList.add(new HomeArea("迅雷专区", HomeType.THUNDER));

        mDB.homeAreaDAO().insertAreas(areaList);
        mDB.homeAreaDAO().deleteAreas();
        List<HomeArea> homeAreas = LiveDataTestUtil.getValue(mDB.homeAreaDAO().getAreas());
        assertThat(homeAreas, notNullValue());
        assertThat(homeAreas.size(), is(0));
    }

    @Test
    public void getAreas() throws Exception {
        List<HomeArea> homeAreas = LiveDataTestUtil.getValue(mDB.homeAreaDAO().getAreas());
        assertThat(homeAreas, notNullValue());
        assertThat(homeAreas.size(), is(0));
    }

    @Test
    public void getAreaById() throws Exception {
        mDB.homeAreaDAO().insertArea(mArea);
        HomeArea homeArea = LiveDataTestUtil.getValue(mDB.homeAreaDAO().getAreaById(mArea.getId()));
        assertThat(homeArea, notNullValue());
        assertThat(homeArea.getId(), is(mArea.getTitle().hashCode()));
    }
}