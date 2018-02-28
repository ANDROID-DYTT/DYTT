package com.bzh.dytt.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "video_detail")
public class VideoDetail {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "link")
    private String mDetailLink;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "years")
    private String mYears;

    @NonNull
    @ColumnInfo(name = "country")
    private String mCountry;

    @NonNull
    @ColumnInfo(name = "category")
    private String mCategory;

    @NonNull
    @ColumnInfo(name = "imdb_grade")
    private String mIMDBGrade;

    @NonNull
    @ColumnInfo(name = "file_size")
    private String mFileSize;

    @NonNull
    @ColumnInfo(name = "duration")
    private String mDuration;

    @NonNull
    @ColumnInfo(name = "director")
    private List<String> mDirector;

    @NonNull
    @ColumnInfo(name = "leading_role")
    private List<String> mLeadingRole;

    @NonNull
    @ColumnInfo(name = "description")
    private String mDescription;

    @NonNull
    @ColumnInfo(name = "cover_url")
    private String mCoverUrl;

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        this.mName = name;
    }

    @NonNull
    public String getYears() {
        return mYears;
    }

    public void setYears(@NonNull String years) {
        mYears = years;
    }

    @NonNull
    public String getCountry() {
        return mCountry;
    }

    public void setCountry(@NonNull String country) {
        mCountry = country;
    }

    @NonNull
    public String getCategory() {
        return mCategory;
    }

    public void setCategory(@NonNull String category) {
        mCategory = category;
    }

    @NonNull
    public String getIMDBGrade() {
        return mIMDBGrade;
    }

    public void setIMDBGrade(@NonNull String IMDBGrade) {
        mIMDBGrade = IMDBGrade;
    }

    @NonNull
    public String getFileSize() {
        return mFileSize;
    }

    public void setFileSize(@NonNull String fileSize) {
        mFileSize = fileSize;
    }

    @NonNull
    public String getDuration() {
        return mDuration;
    }

    public void setDuration(@NonNull String duration) {
        mDuration = duration;
    }

    @NonNull
    public List<String> getDirector() {
        return mDirector;
    }

    public void setDirector(@NonNull List<String> director) {
        mDirector = director;
    }

    @NonNull
    public List<String> getLeadingRole() {
        return mLeadingRole;
    }

    public void setLeadingRole(@NonNull List<String> leadingRole) {
        mLeadingRole = leadingRole;
    }

    @NonNull
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(@NonNull String description) {
        mDescription = description;
    }

    @NonNull
    public String getDetailLink() {
        return mDetailLink;
    }

    public void setDetailLink(@NonNull String detailLink) {
        mDetailLink = detailLink;
    }

    @NonNull
    public String getCoverUrl() {
        return mCoverUrl;
    }

    public void setCoverUrl(@NonNull String coverUrl) {
        mCoverUrl = coverUrl;
    }
}
