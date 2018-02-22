package com.bzh.dytt.data.source;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DyttService {


    @GET("/")
    Call<ResponseBody> getHomePage();
}
