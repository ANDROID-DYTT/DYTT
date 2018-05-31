package com.bzh.dytt

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bzh.dytt.data.ExceptionType
import com.bzh.dytt.data.Resource
import com.bzh.dytt.data.Status
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.single_list_page.*


private const val TAG = "SingleListFragment"

abstract class SingleListFragment<T> : BaseFragment() {

    protected var mOtherExceptionObserver: Observer<Resource<ExceptionType>> = Observer { result -> onOtherException(result) }

    protected var mRefreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener { doRefresh() }

    protected var mListObserver: Observer<Resource<List<T>>> = Observer { result ->

        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE

        when (result?.status) {

            Status.ERROR -> {
                mSwipeRefresh.isRefreshing = false
                mError.visibility = View.VISIBLE
            }

            Status.LOADING -> {
                mSwipeRefresh.isRefreshing = true
            }

            Status.SUCCESS -> {
                mSwipeRefresh.isRefreshing = false
                if (result.data == null || result.data.isEmpty()) {
                    mEmpty.visibility = View.VISIBLE
                } else {
                    if (IsScrollIdle()) {
                        replace(result.data)
                    }
                }
            }
        }
    }

    protected val mScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            mIsScrollIdle = newState == SCROLL_STATE_IDLE
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    protected lateinit var mAdapter: RecyclerView.Adapter<*>

    protected lateinit var mViewModel: ViewModel

    protected lateinit var mSwipeRefresh: SwipeRefreshLayout

    protected lateinit var mRecyclerView: RecyclerView

    protected lateinit var mEmpty: View

    protected lateinit var mError: View

    private var mIsScrollIdle = true

    override fun doCreate(savedInstanceState: Bundle?) {
        super.doCreate(savedInstanceState)
    }

    override fun doCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.single_list_page, container, false)
    }

    override fun doViewCreated(view: View, savedInstanceState: Bundle?) {
        super.doViewCreated(view, savedInstanceState)

        mSwipeRefresh = swipe_refresh_layout
        mRecyclerView = listview
        mEmpty = empty_layout
        mError = error_layout

        mViewModel = createViewModel()
        mSwipeRefresh.setOnRefreshListener(mRefreshListener)

        getListLiveData().observe(this, getListObserver())

        if (getThrowableLiveData() != null) {
            getThrowableLiveData()?.observe(this, getThrowableObserver())
        }

        mAdapter = createAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = getAdapter()
        mRecyclerView.addOnScrollListener(mScrollListener)
    }

    protected open fun doRefresh() {

    }

    protected open fun getListObserver(): Observer<Resource<List<T>>> {
        return mListObserver
    }

    protected open fun onOtherException(result: Resource<ExceptionType>?) {
        if (result?.data == null) {
            return
        }

        when (result.data) {
            ExceptionType.TaskFailure -> {
                if (activity != null) {
                    Toast.makeText(activity, resources.getString(R.string.fetch_video_detail_exception, result.message), Toast.LENGTH_SHORT).show()
                } else {
                    Logger.e(TAG, "onChanged: activity is null")
                }
            }
        }
    }

    protected fun IsScrollIdle() = mIsScrollIdle

    protected open fun getThrowableLiveData(): LiveData<Resource<ExceptionType>>? {
        return null
    }

    protected fun getAdapter(): RecyclerView.Adapter<*> {
        return mAdapter
    }

    protected fun getThrowableObserver(): Observer<Resource<ExceptionType>> {
        return mOtherExceptionObserver
    }

    protected abstract fun createAdapter(): RecyclerView.Adapter<*>

    protected abstract fun replace(listData: List<T>)

    protected abstract fun getListLiveData(): LiveData<Resource<List<T>>>

    protected abstract fun createViewModel(): ViewModel

}