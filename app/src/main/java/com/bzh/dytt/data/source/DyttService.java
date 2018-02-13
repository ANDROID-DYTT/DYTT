package com.bzh.dytt.data.source;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DyttService {

    @GET("/")
    Call<String> getHomePage();
}
