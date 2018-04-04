package com.bzh.dytt.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bzh.dytt.data.MovieCategory;
import com.bzh.dytt.data.VideoDetail;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface VideoDetailDAO {

    @Insert(onConflict = IGNORE)
    void insertVideoDetailList(List<VideoDetail> videoDetailList);

    @Query("SELECT * FROM video_detail WHERE link IN(:links) ORDER BY serial_number DESC")
    LiveData<List<VideoDetail>> getVideoDetails(String[] links);

    @Query("SELECT * FROM video_detail WHERE category =:category ORDER BY serial_number DESC")
    LiveData<List<VideoDetail>> getVideoDetailsByCategory(MovieCategory category);

    @Query("SELECT * FROM video_detail WHERE category =:category AND `query`=:query ORDER BY serial_number DESC")
    LiveData<List<VideoDetail>> getVideoDetailsByCategoryAndQuery(MovieCategory category, String query);

    @Update
    void updateVideoDetail(VideoDetail videoDetail);

    @Query("SELECT is_valid_video_item FROM video_detail WHERE link = :link")
    boolean isValid(String link);

}
