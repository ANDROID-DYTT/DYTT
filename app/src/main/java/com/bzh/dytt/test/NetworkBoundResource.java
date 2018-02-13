package com.bzh.dytt.test;

import android.arch.lifecycle.MediatorLiveData;

public abstract class NetworkBoundResource<ResultType, RequestType> {

//    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
//
//    @MainThread
//    NetworkBoundResource() {
//        result.setValue(Resource.loading(null));
//        LiveData<ResultType> dbSource = loadFromDb();
//        result.addSource(dbSource, data -> {
//            result.removeSource(dbSource);
//            if (shouldFetch(data)) {
//                fetchFromNetwork(dbSource);
//            } else {
//                result.addSource(dbSource,
//                        newData -> result.setValue(Resource.success(newData)));
//            }
//        });
//    }
//
//    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
//        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
//        // we re-attach dbSource as a new source,
//        // it will dispatch its latest value quickly
//        result.addSource(dbSource,
//                newData -> result.setValue(Resource.loading(newData)));
//        result.addSource(apiResponse, response -> {
//            result.removeSource(apiResponse);
//            result.removeSource(dbSource);
//            //noinspection ConstantConditions
//            if (response.isSuccessful()) {
//                saveResultAndReInit(response);
//            } else {
//                onFetchFailed();
//                result.addSource(dbSource,
//                        newData -> result.setValue(
//                                Resource.error(response.errorMessage, newData)));
//            }
//        });
//    }
//
//    @MainThread
//    private void saveResultAndReInit(ApiResponse<RequestType> response) {
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                saveCallResult(response.body);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                // we specially request a new live data,
//                // otherwise we will get immediately last cached value,
//                // which may not be updated with latest results received from network.
//                result.addSource(loadFromDb(),
//                        newData -> result.setValue(Resource.success(newData)));
//            }
//        }.execute();
//    }
//
//    public final LiveData<Resource<ResultType>> getAsLiveData() {
//        return result;
//    }
}