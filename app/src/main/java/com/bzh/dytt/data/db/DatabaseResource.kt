package com.bzh.dytt.data.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import com.bzh.dytt.AppExecutors
import com.bzh.dytt.data.Resource


@MainThread
abstract class DatabaseResource<ResultType>
constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    val asLiveData: LiveData<Resource<ResultType>> = result

    init {

        result.value = Resource.loading<ResultType>(null)

        val dbSource = loadFromDb()

        result.addSource(dbSource) { newData -> result.value = Resource.success(newData) }
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>
}
