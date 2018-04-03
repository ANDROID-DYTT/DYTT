package com.bzh.dytt.data.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bzh.dytt.data.CategoryPage;
import com.bzh.dytt.data.MovieCategory;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface CategoryPageDAO {

    @Insert(onConflict = IGNORE)
    void insertPage(CategoryPage page);

    @Query("SELECT * FROM category_page WHERE category = :category")
    CategoryPage nextPage(MovieCategory category);

    @Update
    void updatePage(CategoryPage page);
}
