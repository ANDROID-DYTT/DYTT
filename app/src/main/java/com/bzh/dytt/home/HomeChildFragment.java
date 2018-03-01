package com.bzh.dytt.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.network.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeChildFragment extends BaseFragment {

    private static final String TAG = "HomeChildFragment";

    public static HomeChildFragment newInstance(HomeArea homeArea) {
        Bundle args = new Bundle();
        args.putParcelable("HOME_AREA", homeArea);
        HomeChildFragment fragment = new HomeChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.home_child_swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.home_child_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.home_child_empty)
    View mEmpty;

    @BindView(R.id.home_child_error)
    View mError;

    @VisibleForTesting
    HomeArea mHomeArea;

    @Inject
    ViewModelProvider.Factory mFactory;

    private HomeChildViewModel mHomeChildViewModel;

    private ChildAdapter mAdapter;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mHomeChildViewModel.getItemsByType(mHomeArea.getType()).observe(HomeChildFragment.this, mObserver);
        }
    };

    private Observer<Resource<List<HomeItem>>> mObserver = new Observer<Resource<List<HomeItem>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<HomeItem>> listResource) {
            mEmpty.setVisibility(View.GONE);
            mError.setVisibility(View.GONE);

            assert listResource != null;
            switch (listResource.status) {
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
                    if (listResource.data == null || listResource.data.isEmpty()) {
                        mEmpty.setVisibility(View.VISIBLE);
                    } else {
                        mAdapter.setItems(listResource.data);
                    }
                }
                break;
            }
        }
    };

    @Override
    protected void doCreate(@Nullable Bundle savedInstanceState) {
        super.doCreate(savedInstanceState);
        Bundle arguments = getArguments();
        assert arguments != null;
        mHomeArea = arguments.getParcelable("HOME_AREA");
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_children, container, false);
    }

    @Override
    protected void doViewCreate(View view, Bundle savedInstanceState) {
        super.doViewCreate(view, savedInstanceState);
        mHomeChildViewModel = ViewModelProviders.of(this, mFactory).get(HomeChildViewModel.class);
        mHomeChildViewModel.getItemsByType(mHomeArea.getType()).observe(this, mObserver);

        mSwipeRefresh.setOnRefreshListener(mRefreshListener);

        mAdapter = new ChildAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

    }

    public class ChildHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_home_child_title)
        TextView mTitle;

        @BindView(R.id.item_home_child_time)
        TextView mTime;

        ChildHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTitle(String title) {
            mTitle.setText(title);
        }

        public void setTime(String time) {
            mTime.setText(time);
        }
    }

    public class ChildAdapter extends RecyclerView.Adapter<ChildHolder> {

        private List<HomeItem> mItems = new ArrayList<>();

        @NonNull
        @Override
        public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_child, parent, false);
            return new ChildHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
            HomeItem homeItem = mItems.get(position);
            holder.setTitle(homeItem.getTitle());
            holder.setTime(homeItem.getTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + homeItem.getTitle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        void setItems(List<HomeItem> items) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }
}
