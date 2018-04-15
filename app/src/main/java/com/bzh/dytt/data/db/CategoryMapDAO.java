package com.bzh.dytt.data.db;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bzh.dytt.data.entity.CategoryMap;
import com.bzh.dytt.data.entity.MovieCategory;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface CategoryMapDAO {

    @Insert(onConflict = IGNORE)
    void insertCategoryMapList(List<CategoryMap> categoryMapList);

    @Query("SELECT * FROM category_map WHERE category = :category ORDER BY serial_number DESC")
    LiveData<List<CategoryMap>> getMovieLinksByCategory(MovieCategory category);

    @Query("SELECT * FROM category_map WHERE category = :category AND `query`=:query ORDER BY serial_number DESC")
    LiveData<List<CategoryMap>> getMovieLinksByCategoryAndQuery(MovieCategory category, String query);

    @Update
    void updateCategory(CategoryMap categoryMap);

}
