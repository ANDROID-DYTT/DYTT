package com.bzh.dytt.util;


import android.content.Context;
import android.support.annotation.NonNull;

import com.bzh.dytt.data.source.DyttService;
import com.bzh.dytt.data.source.HomePageRepository;
import com.bzh.dytt.data.source.MyDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Injection {

    public static HomePageRepository provideHomePageRepository(@NonNull Context context) {
        MyDatabase database = MyDatabase.getInstance(context);

        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://www.dytt8.net")
                .build();

        DyttService dyttService = retrofit.create(DyttService.class);

        return HomePageRepository.getInstance(dyttService, database.homeAreaDAO(), database.homeItemDao());
    }
}
