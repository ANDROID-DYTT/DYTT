package com.bzh.dytt.data.source;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.VisibleForTesting;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.VideoDetail;

@Database(entities = {HomeArea.class, HomeItem.class, VideoDetail.class}, version = 1)
@TypeConverters({DataTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    public static final String DATABASE_NAME = "dytt-data.db";

    public abstract HomeItemDao homeItemDAO();

    public abstract HomeAreaDao homeAreaDAO();

    public abstract VideoDetailDAO videoDetailDAO();

}
