package com.bzh.dytt.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category_page")
public class CategoryPage {

    @PrimaryKey
    @ColumnInfo(name = "category")
    @NonNull
    private MovieCategory mCategory;

    @ColumnInfo(name = "next_page")
    @NonNull
    private int mNextPage;

    public CategoryPage(MovieCategory category, int nextPage) {
        mCategory = category;
        mNextPage = nextPage;
    }

    @NonNull
    public MovieCategory getCategory() {
        return mCategory;
    }

    public void setCategory(@NonNull MovieCategory category) {
        mCategory = category;
    }

    @NonNull
    public int getNextPage() {
        return mNextPage;
    }

    public void setNextPage(@NonNull int nextPage) {
        mNextPage = nextPage;
    }
}
