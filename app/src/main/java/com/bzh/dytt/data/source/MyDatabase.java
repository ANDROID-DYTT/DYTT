package com.bzh.dytt.data.source;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;

@Database(entities = {HomeArea.class, HomeItem.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract HomeItemDao homeItemDao();

    public abstract HomeAreaDao homeAreaDAO();

}
