package com.bzh.dytt.data.network;

import android.arch.lifecycle.LiveData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface DyttService {

    @GET("{detail_link}")
    Call<ResponseBody> getVideoDetail(@Path("detail_link") String detailLink);

    @GET("{category_string}")
    LiveData<ApiResponse<ResponseBody>> getMovieListByCategory(@Path("category_string") String category);

    @GET("{category_string}")
    Call<ResponseBody> getMovieListByCategory2(@Path("category_string") String category);

    @GET
    LiveData<ApiResponse<ResponseBody>> search(@Url String query);

    @GET
    Call<ResponseBody> getSearchVideoDetail(@Url String url);
}
