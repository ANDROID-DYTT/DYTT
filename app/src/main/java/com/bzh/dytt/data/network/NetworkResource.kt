package com.bzh.dytt.data.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.bzh.dytt.AppExecutors
import com.bzh.dytt.data.Resource

@MainThread
abstract class NetworkResource<ResultType, RequestType>
constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        val diskRunnable = Runnable {
            onPreProcess()
            val mainRunnable = Runnable {
                val apiResponse = createCall()
                result.addSource(apiResponse) { response ->
                    result.removeSource(apiResponse)

                    when (response) {
                        is ApiSuccessResponse -> {
                            appExecutors.diskIO().execute {
                                val newData = saveCallResult(processResponse(response))
                                appExecutors.mainThread().execute {
                                    result.value = Resource.success(newData)
                                }
                            }
                        }
                        is ApiEmptyResponse -> {
                            result.value = Resource.success<ResultType>(null)
                        }
                        is ApiErrorResponse -> {
                            onFetchFailed()
                            result.value = Resource.error<ResultType>(response.errorMessage, null)
                        }
                    }
                }
            }
            appExecutors.mainThread().execute(mainRunnable)
        }
        appExecutors.diskIO().execute(diskRunnable)
    }

    @WorkerThread
    protected open fun onPreProcess() {
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType): ResultType

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected open fun onFetchFailed() {
    }

    fun getAsLiveData() = result
}