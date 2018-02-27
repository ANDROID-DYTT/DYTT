package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DyttService {

    @GET("/")
    LiveData<ApiResponse<ResponseBody>> getHomePage();

    @GET("/{detail_link}")
    LiveData<ApiResponse<ResponseBody>> getVideoDetail(@Path("detail_link") String detailLink);

}
