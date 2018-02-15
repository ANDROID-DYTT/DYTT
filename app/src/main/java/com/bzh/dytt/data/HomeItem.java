package com.bzh.dytt.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "homeitems")
public final class HomeItem {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "time")
    private String mTime;

    @ColumnInfo(name = "link")
    @NonNull
    private String mDetailLink;

    @ColumnInfo(name = "type")
    @NonNull
    private int mType;

    public HomeItem() {
    }

    @Ignore
    public HomeItem(String title, String time, String detailLink, int type) {
        this.mTitle = title;
        this.mTime = time;
        this.mDetailLink = detailLink;
        this.mType = type;
        this.mId = detailLink.hashCode();
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    @NonNull
    public String getDetailLink() {
        return mDetailLink;
    }

    public void setDetailLink(@NonNull String detailLink) {
        this.mDetailLink = detailLink;
        this.mId = detailLink.hashCode();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }
}
