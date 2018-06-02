package com.bzh.dytt.vo;


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

    @ColumnInfo(name = "serial_number")
    private int mSN;

    @ColumnInfo(name = "query")
    private String mQuery;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "time")
    private String mTime;

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mTitle) {
        this.mName = mTitle;
    }

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

    @Override
    public String toString() {
        return "CategoryMap{" +
                "mCategory=" + mCategory +
                ", mLink='" + mLink + '\'' +
                ", mSN=" + mSN +
                ", mQuery='" + mQuery + '\'' +
                ", mName='" + mName + '\'' +
                ", mTime='" + mTime + '\'' +
                '}';
    }
}
