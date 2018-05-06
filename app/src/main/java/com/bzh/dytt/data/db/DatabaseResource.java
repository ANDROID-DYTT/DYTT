package com.bzh.dytt.data.db;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bzh.dytt.AppExecutors;
import com.bzh.dytt.data.Resource;

public abstract class DatabaseResource<ResultType> {

    private final AppExecutors mAppExecutors;

    private MediatorLiveData<Resource<ResultType>> mResult = new MediatorLiveData<>();

    @MainThread
    public DatabaseResource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;

        mResult.setValue(Resource.<ResultType>loading(null));

        final LiveData<ResultType> dbSource = loadFromDb();

        mResult.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable final ResultType newData) {
                mResult.setValue(Resource.success(newData));
            }
        });
    }

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // returns a LiveData that represents the resource, implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return mResult;
    }
}
