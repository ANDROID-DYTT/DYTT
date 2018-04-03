package com.bzh.dytt.data.network;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.bzh.dytt.AppExecutors;

public abstract class PureNetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors mAppExecutors;

    private MediatorLiveData<Resource<ResultType>> mResult = new MediatorLiveData<>();

    @MainThread
    public PureNetworkBoundResource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;

        mResult.setValue(Resource.<ResultType>loading(null));

        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        mResult.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestType> response) {
                mResult.removeSource(apiResponse);

                if (response.isSuccessful()) {
                    mAppExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            final ResultType newData = saveCallResult(processResponse(response));
                            mAppExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mResult.setValue(Resource.success(newData));
                                }
                            });
                        }
                    });
                } else {
                    onFetchFailed();
                    mResult.setValue(Resource.<ResultType>error(response.errorMessage, null));
                }
            }
        });
    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    // Called to insertItem the mResult of the API response into the database
    @WorkerThread
    protected abstract ResultType saveCallResult(@NonNull RequestType item);

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed() {
    }

    // returns a LiveData that represents the resource, implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return mResult;
    }
}
