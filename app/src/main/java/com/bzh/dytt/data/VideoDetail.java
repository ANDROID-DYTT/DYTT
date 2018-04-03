package com.bzh.dytt.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;

@Entity(
        tableName = "video_detail",
        indices = {@Index(value = {"link"}, unique = true)}
)
public class VideoDetail {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "link")
    private String mDetailLink;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "years")
    private String mYears;

    @ColumnInfo(name = "country")
    private String mCountry;

    @ColumnInfo(name = "type")
    private String mType;

    @ColumnInfo(name = "imdb_grade")
    private String mIMDBGrade;

    @ColumnInfo(name = "imdb_grade_userds")
    private String mIMDBGradeUsers;

    @ColumnInfo(name = "douban_grade")
    private String mDoubanGrade;

    @ColumnInfo(name = "douban_grade_users")
    private String mDoubanGradeUsers;

    @ColumnInfo(name = "file_size")
    private String mFileSize;

    @ColumnInfo(name = "duration")
    private String mDuration;

    @ColumnInfo(name = "director")
    private List<String> mDirector;

    @ColumnInfo(name = "leading_role")
    private List<String> mLeadingRole;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "cover_url")
    private String mCoverUrl;

    @ColumnInfo(name = "download_link")
    private String mDownloadLink;

    @ColumnInfo(name = "show_time")
    private String mShowTime;

    @ColumnInfo(name = "publish_time")
    private String mPublishTime;

    @ColumnInfo(name = "translation_name")
    private String mTranslationName;

    @ColumnInfo(name = "is_valid_video_item")
    private boolean mValidVideoItem;

    @ColumnInfo(name = "category")
    private MovieCategory mCategory;

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

    public int getSN() {
        return mSN;
    }

    public void setSN(int SN) {
        mSN = SN;
    }

    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        this.mName = name;
    }

    public String getYears() {
        return mYears;
    }

    public void setYears(@NonNull String years) {
        mYears = years;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(@NonNull String country) {
        mCountry = country;
    }

    public String getType() {
        return mType;
    }

    public void setType(@NonNull String type) {
        mType = type;
    }

    public String getIMDBGrade() {
        return mIMDBGrade;
    }

    public void setIMDBGrade(@NonNull String IMDBGrade) {
        mIMDBGrade = IMDBGrade;
    }

    public String getFileSize() {
        return mFileSize;
    }

    public void setFileSize(@NonNull String fileSize) {
        mFileSize = fileSize;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(@NonNull String duration) {
        mDuration = duration;
    }

    public List<String> getDirector() {
        return mDirector;
    }

    public void setDirector(@NonNull List<String> director) {
        mDirector = director;
    }

    public List<String> getLeadingRole() {
        return mLeadingRole;
    }

    public void setLeadingRole(@NonNull List<String> leadingRole) {
        mLeadingRole = leadingRole;
    }

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

    public String getCoverUrl() {
        return mCoverUrl;
    }

    public void setCoverUrl(@NonNull String coverUrl) {
        mCoverUrl = coverUrl;
    }

    public String getDownloadLink() {
        return mDownloadLink;
    }

    public void setDownloadLink(@NonNull String downloadLink) {
        mDownloadLink = downloadLink;
    }

    public String getShowTime() {
        return mShowTime;
    }

    public void setShowTime(@NonNull String showTime) {
        mShowTime = showTime;
    }

    public String getPublishTime() {
        return mPublishTime;
    }

    public void setPublishTime(@NonNull String publishTime) {
        mPublishTime = publishTime;
    }

    public String getDoubanGrade() {
        return mDoubanGrade;
    }

    public void setDoubanGrade(@NonNull String doubanGrade) {
        mDoubanGrade = doubanGrade;
    }

    public String getDoubanGradeUsers() {
        return mDoubanGradeUsers;
    }

    public void setDoubanGradeUsers(String doubanGradeUsers) {
        mDoubanGradeUsers = doubanGradeUsers;
    }

    public String getIMDBGradeUsers() {
        return mIMDBGradeUsers;
    }

    public void setIMDBGradeUsers(String IMDBGradeUsers) {
        mIMDBGradeUsers = IMDBGradeUsers;
    }

    public String getTranslationName() {
        return mTranslationName;
    }

    public void setTranslationName(String translationName) {
        mTranslationName = translationName;
    }

    public boolean isValidVideoItem() {
        return mValidVideoItem;
    }

    public void setValidVideoItem(boolean validVideoItem) {
        mValidVideoItem = validVideoItem;
    }

    public MovieCategory getCategory() {
        return mCategory;
    }

    public void setCategory(MovieCategory category) {
        mCategory = category;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoDetail that = (VideoDetail) o;
        return mSN == that.mSN &&
                Objects.equals(mDetailLink, that.mDetailLink);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mDetailLink, mSN);
    }

    @Override
    public String toString() {
        return "VideoDetail{" +
                "mDetailLink='" + mDetailLink + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }

    public VideoDetail updateValue(VideoDetail videoDetail) {
        setQuery(videoDetail.getQuery());
        setSN(videoDetail.getSN());
        setDetailLink(videoDetail.getDetailLink());
        setCategory(videoDetail.getCategory());
        setQuery(videoDetail.getQuery());
        setValidVideoItem(true);
        return this;
    }

    public VideoDetail updateValue(CategoryMap category) {
        setDetailLink(category.getLink());
        setSN(category.getSN());
        setCategory(category.getCategory());
        setQuery(category.getQuery());
        return this;
    }
}
