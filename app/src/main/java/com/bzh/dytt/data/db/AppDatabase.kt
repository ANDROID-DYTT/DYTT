package com.bzh.dytt.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.support.annotation.VisibleForTesting
import com.bzh.dytt.data.entity.CategoryMap
import com.bzh.dytt.data.entity.CategoryPage
import com.bzh.dytt.data.entity.VideoDetail

@Database(
        entities = [VideoDetail::class, CategoryMap::class, CategoryPage::class],
        version = 2
)
@TypeConverters(DataTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDetailDAO(): VideoDetailDAO

    abstract fun categoryMapDAO(): CategoryMapDAO

    abstract fun categoryPageDAO(): CategoryPageDAO

    companion object {
        @VisibleForTesting
        val DATABASE_NAME = "dytt-data.db"
    }
}
