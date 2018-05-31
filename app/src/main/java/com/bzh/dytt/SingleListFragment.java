package com.bzh.dytt;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bzh.dytt.data.ExceptionType;
import com.bzh.dytt.data.Resource;
import com.orhanobut.logger.Logger;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public abstract class SingleListFragment<T> extends BaseFragment {

    private static final String TAG = "SingleListFragment";

    protected RecyclerView.Adapter mAdapter;

    protected ViewModel mViewModel;

    protected SwipeRefreshLayout mSwipeRefresh;

    protected RecyclerView mRecyclerView;

    protected Observer<Resource<ExceptionType>> mOtherExceptionObserver = new Observer<Resource<ExceptionType>>() {
        @Override
        public void onChanged(@Nullable Resource<ExceptionType> result) {
            onOtherException(result);
        }
    };

    View mEmpty;

    View mError;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            doRefresh();
        }
    };
    private boolean mIsScrollIdle = true;
    protected Observer<Resource<List<T>>> mListObserver = new Observer<Resource<List<T>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<T>> result) {
            mEmpty.setVisibility(View.GONE);
            mError.setVisibility(View.GONE);

            assert result != null;
            switch (result.status) {
                case ERROR: {
                    mSwipeRefresh.setRefreshing(false);
                    mError.setVisibility(View.VISIBLE);
                }
                break;
                case LOADING: {
                    mSwipeRefresh.setRefreshing(true);
                }
                break;
                case SUCCESS: {
                    mSwipeRefresh.setRefreshing(false);
                    if (result.data == null || result.data.isEmpty()) {
                        mEmpty.setVisibility(View.VISIBLE);
                    } else {
                        if (IsScrollIdle()) {
                            replace(result.data);
                        }
                    }
                }
                break;
            }
        }
    };
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            mIsScrollIdle = (newState == SCROLL_STATE_IDLE);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

    };

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_list_page, container, false);
    }

    protected abstract RecyclerView.Adapter createAdapter();

    protected abstract void replace(List<T> listData);

    protected abstract LiveData<Resource<List<T>>> getListLiveData();

    protected LiveData<Resource<ExceptionType>> getThrowableLiveData() {
        return null;
    }

    protected abstract ViewModel createViewModel();

    @Override
    protected void doCreate(@Nullable Bundle savedInstanceState) {
        super.doCreate(savedInstanceState);
    }

    @Override
    protected void doViewCreated(View view, Bundle savedInstanceState) {
        super.doViewCreated(view, savedInstanceState);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.listview);
        mEmpty = view.findViewById(R.id.empty_layout);
        mError = view.findViewById(R.id.error_layout);

        mViewModel = createViewModel();
        mSwipeRefresh.setOnRefreshListener(mRefreshListener);
        if (getListLiveData() != null) {
            getListLiveData().observe(this, getListObserver());
        }
        if (getThrowableLiveData() != null) {
            getThrowableLiveData().observe(this, getThrowableObserver());
        }
        mAdapter = createAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(getAdapter());

        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    protected void doRefresh() {
    }

    public Observer<Resource<List<T>>> getListObserver() {
        return mListObserver;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public Observer<Resource<ExceptionType>> getThrowableObserver() {
        return mOtherExceptionObserver;
    }

    public boolean IsScrollIdle() {
        return mIsScrollIdle;
    }

    protected void onOtherException(Resource<ExceptionType> result) {
        if (result.data != null) {
            switch (result.data) {
                case TaskFailure:
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.fetch_video_detail_exception, result.message), Toast.LENGTH_SHORT).show();
                    } else {
                        Logger.e(TAG, "onChanged: activity is null");
                    }
                    break;
            }
        }
    }
}
