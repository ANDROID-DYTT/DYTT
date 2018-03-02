package com.bzh.dytt.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "homeitems", indices = {@Index(value = {"link", "type"}, unique = true)})
public final class HomeItem {

    @PrimaryKey(autoGenerate = true)
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

    @ColumnInfo(name = "time_millis")
    @NonNull
    private long mTimeMillis;


    public HomeItem() {
    }

    @Ignore
    public HomeItem(String title, String time, String detailLink, int type) {
        this.mTitle = title;
        setTime(time);
        this.mDetailLink = detailLink;
        this.mType = type;
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

    @Ignore
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public void setTime(String time) {
        this.mTime = time;
        if (time != null && time.length() > 0) {
            try {
                setTimeMillis(mFormat.parse(time).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                mTime = null;
                setTimeMillis(System.currentTimeMillis());
            }
        } else {
            setTimeMillis(System.currentTimeMillis());
        }
    }

    @NonNull
    public long getTimeMillis() {
        return mTimeMillis;
    }

    public void setTimeMillis(@NonNull long timeMillis) {
        mTimeMillis = timeMillis;
    }
}
