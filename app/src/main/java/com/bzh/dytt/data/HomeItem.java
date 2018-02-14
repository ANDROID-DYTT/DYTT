package com.bzh.dytt.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "homeitems")
public final class HomeItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "time")
    private String mTime;

    @ColumnInfo(name = "link")
    private String mDetailLink;

    @ColumnInfo(name = "type")
    private int mType;

    public HomeItem() {
    }

    @Ignore
    public HomeItem(int id, String title, String time, String detailLink, int type) {
        this.mId = id;
        this.mTitle = title;
        this.mTime = time;
        this.mDetailLink = detailLink;
        this.mType = type;
    }

    @Ignore
    public HomeItem(String title, String time, String detailLink, int type) {
        this.mTitle = title;
        this.mTime = time;
        this.mDetailLink = detailLink;
        this.mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDetailLink() {
        return mDetailLink;
    }

    public void setDetailLink(String detailLink) {
        this.mDetailLink = detailLink;
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
