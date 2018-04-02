package com.bzh.dytt.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.bzh.dytt.AppExecutors;

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 * <p>
 * You can read more about it in the <a href="https://developer.android.com/arch">Architecture
 * Guide</a>.
 * <p>
 * 有关NetworkBoundResource的设计意图可以参考： https://developer.android.com/topic/libraries/architecture/guide.html#addendum
 *
 * Test Case :
 * 1. db success without network
 * 2. db success with network success
 * 3. db success with network failure
 * 4. network success
 * 5. network failure
 *
 * @param <ResultType>
 * @param <RequestType>
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors mAppExecutors;

    private MediatorLiveData<Resource<ResultType>> mResult = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;

        mResult.setValue(Resource.<ResultType>loading(null));

        final LiveData<ResultType> dbSource = loadFromDb();

        mResult.addSource(dbSource, new Observer<ResultType>() {

            @Override
            public void onChanged(@Nullable ResultType data) {
                mResult.removeSource(dbSource);

                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource);
                } else {
                    mResult.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType newData) {
                            mResult.setValue(Resource.success(newData));
                        }
                    });
                }
            }
        });

    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {

        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        mResult.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType newData) {
                mResult.setValue(Resource.loading(newData));
            }
        });

        mResult.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestType> response) {

                mResult.removeSource(apiResponse);
                mResult.removeSource(dbSource);

                //noinspection ConstantConditions
                if (response.isSuccessful()) {
                    mAppExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCallResult(processResponse(response));
                            mAppExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    // we specially request a new live data,
                                    // otherwise we will get immediately last cached value,
                                    // which may not be updated with latest results received from network.
                                    mResult.addSource(loadFromDb(), new Observer<ResultType>() {
                                        @Override
                                        public void onChanged(@Nullable ResultType newData) {
                                            mResult.setValue(Resource.success(newData));
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    onFetchFailed();

                    mResult.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType newData) {
                            mResult.setValue(Resource.error(response.errorMessage, newData));
                        }
                    });
                }
            }
        });
    }

    // returns a LiveData that represents the resource, implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return mResult;
    }

    // Called to insertItem the mResult of the API response into the database
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed() {
    }
}