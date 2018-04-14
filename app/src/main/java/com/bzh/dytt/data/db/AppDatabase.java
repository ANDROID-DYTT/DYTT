package com.bzh.dytt.data.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.VisibleForTesting;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.CategoryPage;
import com.bzh.dytt.data.VideoDetail;

@Database(entities = {VideoDetail.class, CategoryMap.class, CategoryPage.class}, version = 2)
@TypeConverters({DataTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    public static final String DATABASE_NAME = "dytt-data.db";

    public abstract VideoDetailDAO videoDetailDAO();

    public abstract CategoryMapDAO categoryMapDAO();

    public abstract CategoryPageDAO categoryPageDAO();


}
