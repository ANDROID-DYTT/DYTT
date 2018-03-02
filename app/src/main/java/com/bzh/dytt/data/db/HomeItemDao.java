package com.bzh.dytt.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bzh.dytt.data.HomeItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;


@Dao
public interface HomeItemDao {

    @Query("SELECT * FROM homeitems")
    LiveData<List<HomeItem>> getItems();

    @Query("SELECT * FROM homeitems WHERE type = :type ORDER BY time_millis DESC")
    LiveData<List<HomeItem>> getItemsByType(int type);

    @Query("SELECT * FROM homeitems WHERE id = :id")
    LiveData<HomeItem> getItemById(int id);

    @Insert(onConflict = IGNORE)
    void insertItem(HomeItem item);

    @Insert(onConflict = IGNORE)
    void insertItems(List<HomeItem> items);

    @Update
    void updateItem(HomeItem item);

    @Query("DELETE FROM homeitems WHERE id = :id")
    int deleteItemById(int id);

    @Delete
    void deleteItem(HomeItem item);

    @Query("DELETE FROM homeitems")
    void deleteItems();

}
