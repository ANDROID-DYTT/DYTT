package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bzh.dytt.data.HomeArea;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface HomeAreaDao {

    @Insert(onConflict = REPLACE)
    void insertArea(HomeArea area);

    @Insert(onConflict = REPLACE)
    void insertAreas(List<HomeArea> areas);

    @Update
    void updateArea(HomeArea area);

    @Delete
    void deleteArea(HomeArea area);

    @Query("DELETE FROM homearea")
    void deleteAreas();

    @Query("SELECT * FROM homearea")
    LiveData<List<HomeArea>> getAreas();

    @Query("SELECT * FROM homearea WHERE id = :id")
    LiveData<HomeArea> getAreaById(int id);
}
