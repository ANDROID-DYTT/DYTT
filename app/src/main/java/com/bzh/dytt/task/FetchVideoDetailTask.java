package com.bzh.dytt.task;

import android.arch.lifecycle.MutableLiveData;

import com.bzh.dytt.data.ExceptionType;
import com.bzh.dytt.data.Resource;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.entity.VideoDetail;
import com.bzh.dytt.data.network.ApiResponse;
import com.bzh.dytt.data.network.ApiSuccessResponse;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.util.VideoDetailPageParser;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FetchVideoDetailTask implements Runnable {

    private final VideoDetail mVideoDetail;
    private final VideoDetailDAO mVideoDetailDAO;
    private final DyttService mService;
    private final VideoDetailPageParser mParser;
    private MutableLiveData<Resource<ExceptionType>> mFetchDetailState;

    public FetchVideoDetailTask(VideoDetail videoDetail, VideoDetailDAO videoDetailDAO, DyttService service, VideoDetailPageParser parser, MutableLiveData<Resource<ExceptionType>> fetchDetailState) {
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
            ApiResponse<ResponseBody> apiResponse = ApiSuccessResponse.Companion.create(response);
            if (apiResponse instanceof ApiSuccessResponse) {
                VideoDetail videoDetail = mParser.parseVideoDetail(new String(((ApiSuccessResponse<ResponseBody>) apiResponse).getBody().bytes(), "GB2312"));
                videoDetail.updateValue(mVideoDetail);
                mVideoDetailDAO.updateVideoDetail(videoDetail);
            }
        } catch (IOException e) {
            mFetchDetailState.postValue(Resource.Companion.error(e.getMessage(), ExceptionType.TaskFailure));
            Logger.e("FetchVideoDetailTask", "Something wrong when fetch video detail " + e.getMessage());
        }
    }
}
