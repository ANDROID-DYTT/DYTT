package com.bzh.dytt.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class HomeItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public String time;

    public String detailLink;

    public int type;

    public HomeItem() {
    }


    public HomeItem(int id, String name, String time, String detailLink, int type) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
    }

    public HomeItem(String name, String time, String detailLink, int type) {
        this.name = name;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
