package com.bzh.dytt.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.network.Resource;

import java.util.List;

import butterknife.BindView;

public abstract class SingleListFragment<T> extends BaseFragment {

    private static final String TAG = "SingleListFragment";

    protected RecyclerView.Adapter mAdapter;

    protected ViewModel mViewModel;

    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.listview)
    protected RecyclerView mRecyclerView;
    protected Observer<Throwable> mThrowableObserver = new Observer<Throwable>() {
        @Override
        public void onChanged(@Nullable Throwable throwable) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.fetch_video_detail_exception, throwable.getMessage()), Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onChanged: activity is null");
            }
        }
    };
    @BindView(R.id.empty_layout)
    View mEmpty;
    @BindView(R.id.error_layout)
    View mError;
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
                        replace(result.data);
                    }
                }
                break;
            }
        }
    };
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            doRefresh();
        }
    };

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_list_page, container, false);
    }

    protected abstract RecyclerView.Adapter createAdapter();

    protected abstract void replace(List<T> listData);

    protected abstract LiveData<Resource<List<T>>> getListLiveData();

    protected LiveData<Throwable> getThrowableLiveData() {
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
    }

    protected void doRefresh() {
    }

    public Observer<Resource<List<T>>> getListObserver() {
        return mListObserver;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public Observer<Throwable> getThrowableObserver() {
        return mThrowableObserver;
    }
}
