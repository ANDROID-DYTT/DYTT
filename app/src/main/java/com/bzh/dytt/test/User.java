package com.bzh.dytt.test;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
class User {
    @PrimaryKey
    private int id;
    private String name;
    private String lastName;
    // getters and setters for fields
}