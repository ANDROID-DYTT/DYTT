package com.bzh.dytt.data.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;


public abstract class AppDatabaseTest {

    protected AppDatabase mDB;

    @Before
    public void initDB() throws Exception {
        mDB = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase.class).build();
    }

    @After
    public void closeDB() throws Exception {
        mDB.close();
    }

}