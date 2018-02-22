package com.bzh.dytt.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;

import java.util.List;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class HomePageRepository {

    private DyttService mService;
    private HomeItemDao mHomeItemDao;
    private HomeAreaDao mHomeAreaDao;

    public void setService(DyttService service) {
        mService = service;
    }

    public void setHomeItemDao(HomeItemDao dao) {
        mHomeItemDao = dao;
    }

    public void setHomeAreaDao(HomeAreaDao dao) {
        mHomeAreaDao = dao;
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

                mService.getHomePage().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        liveData.setValue(Resource.success(response.body()));
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
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

                mService.getHomePage().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        liveData.setValue(Resource.success(response.body()));
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        liveData.setValue(Resource.<String>error(t.getMessage(), null));
                    }
                });
                return liveData;

            }
        }.getAsLiveData();
    }
}
