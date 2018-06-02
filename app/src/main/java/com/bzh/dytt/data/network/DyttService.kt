package com.bzh.dytt.data.network

import android.arch.lifecycle.LiveData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface DyttService {

    @GET("{detail_link}")
    fun getVideoDetail(@Path("detail_link") detailLink: String): Call<ResponseBody>

    @GET("{category_string}")
    fun getMovieListByCategory(@Path("category_string") category: String): LiveData<ApiResponse<ResponseBody>>

    @GET
    fun search(@Url query: String): LiveData<ApiResponse<ResponseBody>>

    @GET
    fun getSearchVideoDetail(@Url url: String): Call<ResponseBody>
}
