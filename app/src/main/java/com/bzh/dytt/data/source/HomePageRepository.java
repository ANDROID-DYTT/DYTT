package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageRepository {

    private volatile static HomePageRepository INSTANCE = null;

    private DyttService mService;
    private HomeItemDao mHomeItemDao;
    private HomeAreaDao mHomeAreaDao;

    public HomePageRepository(DyttService service, HomeAreaDao dao, HomeItemDao homeItemDao) {
        mService = service;
        mHomeItemDao = homeItemDao;
        mHomeAreaDao = dao;
    }

    public static HomePageRepository getInstance(DyttService service, HomeAreaDao homeAreaDao, HomeItemDao homeItemDao) {
        if (INSTANCE == null) {
            synchronized (HomePageRepository.class) {
                if (INSTANCE == null) {

                    INSTANCE = new HomePageRepository(service, homeAreaDao, homeItemDao);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<Resource<List<HomeArea>>> getHomeAreas() {
        return new NetworkBoundResource<List<HomeArea>, String>() {

            @Override
            protected void saveCallResult(@NonNull String item) {
                List<HomeArea> homeAreas = HomeItemParseUtil.getInstance().parseAreas(item);
                mHomeAreaDao.insertAreas(homeAreas);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<HomeArea> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<HomeArea>> loadFromDb() {
                return mHomeAreaDao.getAreas();
            }

            @NonNull
            @Override
            protected LiveData<Resource<String>> createCall() {

                final MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
                mService.getHomePage().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            liveData.setValue(Resource.success(new String(response.body().bytes(), "GB2312")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        liveData.setValue(Resource.<String>error(t.getMessage(), null));
                    }
                });
                return liveData;
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<List<HomeItem>>> getHomeItems(final int type) {
        return new NetworkBoundResource<List<HomeItem>, String>() {

            @Override
            protected void saveCallResult(@NonNull String item) {
                try {
                    item = new String(item.getBytes(), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                List<HomeItem> homeItems = HomeItemParseUtil.getInstance().parseItems(item);
                mHomeItemDao.insertItems(homeItems);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<HomeItem> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<HomeItem>> loadFromDb() {
                return mHomeItemDao.getItemsByType(type);
            }

            @NonNull
            @Override
            protected LiveData<Resource<String>> createCall() {

                final MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();

                mService.getHomePage().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            liveData.setValue(Resource.success(new String(response.body().bytes(), "GB2312")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        liveData.setValue(Resource.<String>error(t.getMessage(), null));
                    }
                });
                return liveData;

            }
        }.getAsLiveData();
    }
}
