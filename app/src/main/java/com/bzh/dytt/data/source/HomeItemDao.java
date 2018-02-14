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

    @Query("SELECT * FROM homeitems")
    LiveData<List<HomeItem>> getItems();

    @Query("SELECT * FROM homeitems WHERE type = :type ORDER BY id")
    LiveData<List<HomeItem>> getItemsByType(int type);

    @Query("SELECT * FROM homeitems WHERE id = :id")
    LiveData<HomeItem> getItemById(int id);

    @Insert(onConflict = REPLACE)
    void insertItem(HomeItem item);

    @Insert(onConflict = REPLACE)
    void insertItems(List<HomeItem> items);

    @Update
    void updateItem(HomeItem item);

    @Query("DELETE FROM homeitems WHERE id = :id")
    int deleteItemById(int id);

    @Query("DELETE FROM homeitems")
    void deleteItems();

}
