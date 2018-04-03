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

    @BindView(R.id.empty_layout)
    View mEmpty;

    @BindView(R.id.error_layout)
    View mError;

    protected Observer<Resource<List<T>>> mObserver = new Observer<Resource<List<T>>>() {
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
                    Log.d(TAG, "onChanged() called with: result = [" + result + "]");
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

    protected abstract LiveData<Resource<List<T>>> getLiveData();

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
        if (getLiveData() != null) {
            getLiveData().observe(this, mObserver);
        }
        mAdapter = createAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void doRefresh() {
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }
}
