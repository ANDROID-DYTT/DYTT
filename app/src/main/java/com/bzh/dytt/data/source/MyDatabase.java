package com.bzh.dytt.data.source;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.bzh.dytt.data.HomeItem;

@Database(entities = {HomeItem.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract HomeItemDao itemDao();

}
