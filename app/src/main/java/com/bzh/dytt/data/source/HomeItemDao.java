package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bzh.dytt.data.HomeItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface HomeItemDao {

    @Query("SELECT * FROM HomeItem")
    LiveData<List<HomeItem>> getItems();

    @Query("SELECT * FROM HomeItem WHERE type = :type ORDER BY id")
    LiveData<List<HomeItem>> getItemsByType(int type);

    @Query("SELECT * FROM HomeItem WHERE id = :id")
    LiveData<HomeItem> getItemById(int id);

    @Insert(onConflict = REPLACE)
    void insertItem(HomeItem item);

    @Update
    void updateItem(HomeItem item);

    @Query("DELETE FROM HomeItem WHERE id = :id")
    int deleteItemById(int id);

    @Query("DELETE FROM HomeItem")
    void deleteItems();
}
