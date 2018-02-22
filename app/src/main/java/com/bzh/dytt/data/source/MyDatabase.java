package com.bzh.dytt.data.source;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;

@Database(entities = {HomeArea.class, HomeItem.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase INSTANCE;

    private static final Object sLock = new Object();

    public abstract HomeItemDao homeItemDao();

    public abstract HomeAreaDao homeAreaDAO();

    public static MyDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "MyData.db").build();
            }
            return INSTANCE;
        }
    }
}
