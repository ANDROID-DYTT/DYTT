package com.bzh.dytt.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(
        tableName = "category_map",
        primaryKeys = {"category", "link"}
)
public class CategoryMap {

    @ColumnInfo(name = "category")
    @NonNull
    private MovieCategory mCategory;

    @ColumnInfo(name = "link")
    @NonNull
    private String mLink;

    @ColumnInfo(name = "is_parsed")
    private boolean mIsParsed;

    @ColumnInfo(name = "serial_number")
    private int mSN;

    @ColumnInfo(name = "query")
    private String mQuery;

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    @NonNull
    public MovieCategory getCategory() {
        return mCategory;
    }

    public void setCategory(@NonNull MovieCategory category) {
        mCategory = category;
    }

    public boolean getIsParsed() {
        return mIsParsed;
    }

    public void setIsParsed(boolean mIsParsed) {
        this.mIsParsed = mIsParsed;
    }

    public int getSN() {
        return mSN;
    }

    public void setSN(int SN) {
        mSN = SN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryMap)) return false;

        CategoryMap that = (CategoryMap) o;

        if (getCategory() != that.getCategory()) return false;
        return getLink().equals(that.getLink());
    }

    @Override
    public int hashCode() {
        int result = getCategory().hashCode();
        result = 31 * result + getLink().hashCode();
        return result;
    }
}
