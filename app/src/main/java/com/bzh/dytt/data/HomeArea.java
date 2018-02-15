package com.bzh.dytt.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "homearea")
public class HomeArea {

    @ColumnInfo(name = "id")
    @PrimaryKey
    private int mId;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "type")
    private int mType;

    @ColumnInfo(name = "link")
    private String mDetailLink;

    public HomeArea() {

    }

    @Ignore
    public HomeArea(@NonNull String title, @NonNull int type) {
        mTitle = title;
        mId = title.hashCode();
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        this.mTitle = title;
        this.mId = title.hashCode();
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getDetailLink() {
        return mDetailLink;
    }

    public void setDetailLink(String detailLink) {
        mDetailLink = detailLink;
    }
}
