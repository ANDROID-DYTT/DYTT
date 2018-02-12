package com.bzh.dytt.services;

import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceManager {

    private static ServiceManager sManager;

    public static ServiceManager getInstance() {
        if (sManager == null) {
            sManager = new ServiceManager();
        }
        return sManager;
    }

    private ColorHuntService mColorHuntService;

    public ColorHuntService getColorHuntService() {
//        if (mColorHuntService == null) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .baseUrl("http://colorhunt.co/")
//                    .build();
//
//            mColorHuntService = retrofit.create(ColorHuntService.class);
//        }
        return mColorHuntService;
    }
}
