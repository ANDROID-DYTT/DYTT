package com.bzh.dytt.home;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Home {

    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }
}
