package com.bzh.dytt.task;


import android.text.TextUtils;
import android.util.Log;

import com.bzh.dytt.data.CategoryMap;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.db.CategoryMapDAO;
import com.bzh.dytt.data.db.VideoDetailDAO;
import com.bzh.dytt.data.network.ApiResponse;
import com.bzh.dytt.data.network.DyttService;
import com.bzh.dytt.util.VideoDetailPageParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FetchSearchVideoDetailTask implements Runnable {

    private static final String TAG = "FetchVideoDetailTask";

    private final CategoryMap mCategoryMap;
    private final DyttService mService;
    private final CategoryMapDAO mCategoryMapDAO;
    private final VideoDetailDAO mVideoDetailDAO;
    private VideoDetailPageParser mParser;
    private String mQuery;

    public FetchSearchVideoDetailTask(CategoryMap categoryMap, CategoryMapDAO categoryMapDAO, VideoDetailDAO videoDetailDAO, DyttService service, VideoDetailPageParser parser, String query) {
        mCategoryMap = categoryMap;
        mService = service;
        mParser = parser;
        mCategoryMapDAO = categoryMapDAO;
        mVideoDetailDAO = videoDetailDAO;
        mQuery = query;
    }

    @Override
    public void run() {
        try {
            Response<ResponseBody> response = mService.getSearchVideoDetail("http://www.ygdy8.com" + mCategoryMap.getLink()).execute();
            ApiResponse<ResponseBody> apiResponse = new ApiResponse<>(response);

            if (apiResponse.isSuccessful()) {
                VideoDetail videoDetail = mParser.parseVideoDetail(new String(apiResponse.body.bytes(), "GB2312"));
                if (TextUtils.isEmpty(videoDetail.getName())) {
                    videoDetail.setValidVideoItem(false);
                } else {
                    videoDetail.setValidVideoItem(true);
                }
                videoDetail.setSN(mCategoryMap.getSN());
                videoDetail.setDetailLink(mCategoryMap.getLink());
                videoDetail.setCategory(mCategoryMap.getCategory());
                videoDetail.setQuery(mQuery);
                mVideoDetailDAO.updateVideoDetail(videoDetail);
                mCategoryMap.setIsParsed(true);
                mCategoryMapDAO.updateCategory(mCategoryMap);
            }
        } catch (IOException e) {
            Log.e("FetchVideoDetailTask", "Something wrong when fetch video detail " + e.getMessage());
        }
    }
}
