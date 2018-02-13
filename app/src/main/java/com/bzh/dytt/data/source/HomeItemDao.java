package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bzh.dytt.data.HomeItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface HomeItemDao {

    @Insert(onConflict = REPLACE)
    void save(HomeItem item);

    @Query("SELECT * FROM HomeItem WHERE id = :itemId")
    LiveData<HomeItem> load(int itemId);

    @Query("SELECT * FROM HomeItem")
    LiveData<List<HomeItem>> loadAll();

    @Query("SELECT * FROM HomeItem WHERE type = :type ORDER BY id")
    LiveData<List<HomeItem>> loadNewest(int type);
}
