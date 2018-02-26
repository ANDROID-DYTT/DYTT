package com.bzh.dytt.di;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.bzh.dytt.data.source.AppDatabase;
import com.bzh.dytt.data.source.DyttService;
import com.bzh.dytt.data.source.HomeAreaDao;
import com.bzh.dytt.data.source.HomeItemDao;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * https://github.com/codepath/android_guides/wiki/Dependency-Injection-with-Dagger-2
 */
@Module(includes = ViewModelModule.class)
public class AppModule {

    // "http://www.dytt8.net")
    private String mBaseUrl;

    public AppModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Singleton
    @Provides
    Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=60")
                        .removeHeader("Pragma")
                        .build();
            }
        };
    }

    @Singleton
    @Provides
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Cache okhttpCache, Interceptor okHttpCacheInterceptor) {
        return new OkHttpClient.Builder()
                .cache(okhttpCache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(okHttpCacheInterceptor)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return    new Retrofit
                .Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    DyttService provideDyttService(Retrofit retrofit) {
        return retrofit.create(DyttService.class);
    }

    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    HomeAreaDao provideHomeAreaDao(AppDatabase db) {
        return db.homeAreaDAO();
    }

    @Singleton
    @Provides
    HomeItemDao provideHomeItemDao(AppDatabase db) {
        return db.homeItemDao();
    }
}
