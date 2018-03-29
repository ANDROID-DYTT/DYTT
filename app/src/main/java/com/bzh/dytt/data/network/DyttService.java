package com.bzh.dytt.data.network;

import android.arch.lifecycle.LiveData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DyttService {

    @GET("/")
    LiveData<ApiResponse<ResponseBody>> getHomePage();

    @GET("/{detail_link}")
    Call<ResponseBody> getVideoDetailNew(@Path("detail_link") String detailLink);

    @GET("/html/gndy/{category_string}")
    LiveData<ApiResponse<ResponseBody>> getMovieListByCategory(@Path("category_string") String category);

    @GET("/html/gndy/{category_string}")
    Call<ResponseBody> getMovieListByCategory2(@Path("category_string") String category);

    @GET("http://s.ygdy8.com/plus/so.php?kwtype=0&searchtype=title&keyword={query}")
    LiveData<ApiResponse<ResponseBody>> search(@Path("query") String query);
}
