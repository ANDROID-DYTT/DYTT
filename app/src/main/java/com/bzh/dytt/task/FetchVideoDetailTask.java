package com.bzh.dytt.task;


import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.network.ApiResponse;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.util.VideoDetailPageParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FetchVideoDetailTask implements Runnable {

    private final VideoDetail mVideoDetail;
    private final VideoDetailDAO mVideoDetailDAO;
    private final DyttService mService;
    private final VideoDetailPageParser mParser;
    private MutableLiveData<Throwable> mFetchDetailState;

    public FetchVideoDetailTask(VideoDetail videoDetail, VideoDetailDAO videoDetailDAO, DyttService service, VideoDetailPageParser parser, MutableLiveData<Throwable> fetchDetailState) {
        mVideoDetail = videoDetail;
        mVideoDetailDAO = videoDetailDAO;
        mService = service;
        mParser = parser;
        mFetchDetailState = fetchDetailState;
    }

    @Override
    public void run() {
        try {
            Response<ResponseBody> response = mService.getVideoDetail(mVideoDetail.getDetailLink()).execute();
            ApiResponse<ResponseBody> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                VideoDetail videoDetail = mParser.parseVideoDetail(new String(apiResponse.body.bytes(), "GB2312"));
                videoDetail.updateValue(mVideoDetail);
                mVideoDetailDAO.updateVideoDetail(videoDetail);
            }
        } catch (IOException e) {
            mFetchDetailState.postValue(e);
            Log.e("FetchVideoDetailTask", "Something wrong when fetch video detail " + e.getMessage());
        }
    }
}
