package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.data.HomeArea;
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
public class HomeAreaDaoTest {

    private HomeArea mArea = new HomeArea("2018新片精品", HomeType.NEWEST);

    private MyDatabase mDatabase;
    private HomeAreaDao mHomeAreaDao;

    @Before
    public void setUp() throws Exception {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), MyDatabase.class).build();
        mHomeAreaDao = mDatabase.homeAreaDAO();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insertArea() throws Exception {

        LiveData<HomeArea> area = mHomeAreaDao.getAreaById(mArea.getId());
        area.observeForever(new Observer<HomeArea>() {
            @Override
            public void onChanged(@Nullable HomeArea homeArea) {
                assertThat(homeArea, notNullValue());
                assertThat(homeArea.getId(), is(mArea.getId()));
                assertThat(homeArea.getTitle(), is(mArea.getTitle()));
            }
        });

        mHomeAreaDao.insertArea(mArea);
    }

    @Test
    public void insertAreas() throws Exception {

        List<HomeArea> areaList = new ArrayList<>();
        areaList.add(new HomeArea("最新电影", HomeType.NEWEST));
        areaList.add(new HomeArea("迅雷专区", HomeType.THUNDER));

        mHomeAreaDao.getAreas().observeForever(new Observer<List<HomeArea>>() {
            @Override
            public void onChanged(@Nullable List<HomeArea> homeAreas) {
                assertThat(homeAreas, notNullValue());
                assertThat(homeAreas.size(), is(2));
            }
        });

        mHomeAreaDao.insertAreas(areaList);
    }

    @Test
    public void updateArea() throws Exception {

        mHomeAreaDao.insertArea(mArea);

        LiveData<HomeArea> area = mHomeAreaDao.getAreaById(mArea.getId());
        area.observeForever(new Observer<HomeArea>() {
            @Override
            public void onChanged(@Nullable HomeArea homeArea) {
                assertThat(homeArea, notNullValue());
                assertThat(homeArea.getType(), is(HomeType.FILM));
            }
        });

        mArea.setType(HomeType.FILM);
        mHomeAreaDao.updateArea(mArea);
    }

    @Test
    public void deleteArea() throws Exception {

        mHomeAreaDao.insertArea(mArea);

        mHomeAreaDao.getAreas().observeForever(new Observer<List<HomeArea>>() {
            @Override
            public void onChanged(@Nullable List<HomeArea> homeAreas) {
                assertThat(homeAreas, notNullValue());
                assertThat(homeAreas.size(), is(0));
            }
        });

        mHomeAreaDao.deleteArea(mArea);

    }

    @Test
    public void deleteAreas() throws Exception {
        List<HomeArea> areaList = new ArrayList<>();
        areaList.add(new HomeArea("最新电影", HomeType.NEWEST));
        areaList.add(new HomeArea("迅雷专区", HomeType.THUNDER));

        mHomeAreaDao.insertAreas(areaList);

        mHomeAreaDao.getAreas().observeForever(new Observer<List<HomeArea>>() {
            @Override
            public void onChanged(@Nullable List<HomeArea> homeAreas) {
                assertThat(homeAreas, notNullValue());
                assertThat(homeAreas.size(), is(0));
            }
        });

        mHomeAreaDao.deleteAreas();

    }

    @Test
    public void getAreas() throws Exception {
        mHomeAreaDao.getAreas().observeForever(new Observer<List<HomeArea>>() {
            @Override
            public void onChanged(@Nullable List<HomeArea> homeAreas) {
                assertThat(homeAreas, notNullValue());
                assertThat(homeAreas.size(), is(0));
            }
        });
    }

    @Test
    public void getAreaById() throws Exception {


        LiveData<HomeArea> area = mHomeAreaDao.getAreaById(mArea.getId());
        area.observeForever(new Observer<HomeArea>() {
            @Override
            public void onChanged(@Nullable HomeArea homeArea) {
                assertThat(homeArea, notNullValue());
                assertThat(homeArea.getId(), is(mArea.getTitle().hashCode()));
            }
        });

        mHomeAreaDao.insertArea(mArea);

    }

}