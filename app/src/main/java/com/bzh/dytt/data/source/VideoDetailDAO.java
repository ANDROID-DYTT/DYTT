package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bzh.dytt.data.VideoDetail;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VideoDetailDAO {

    @Insert(onConflict = REPLACE)
    void insertVideoDetail(VideoDetail videoDetail);

    @Update
    void updateVideoDetail(VideoDetail videoDetail);

    @Delete
    void deleteVideoDetail(VideoDetail videoDetail);

    @Query("DELETE FROM video_detail")
    void deleteVideoDetails();

    @Query("SELECT * FROM video_detail")
    LiveData<List<VideoDetail>> getVideoDetails();

    @Query("SELECT * FROM video_detail WHERE link = :detailLink")
    LiveData<VideoDetail> getVideoDetailByLink(String detailLink);
}
