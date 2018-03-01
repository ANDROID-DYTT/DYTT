package com.bzh.dytt.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.data.network.Status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HomePageFragment extends BaseFragment {

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    private static final String TAG = "HomePageFragment";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    HomePageViewModel mHomeViewModel;

    HomeTabAdapter mHomeTabAdapter;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mHomeViewModel.getHomeArea().observe(HomePageFragment.this, mHomeResourceObserver);
        }
    };

    private Observer<Resource<List<HomeArea>>> mHomeResourceObserver = new Observer<Resource<List<HomeArea>>>() {

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onChanged(@Nullable Resource<List<HomeArea>> result) {

            mLoadError.setVisibility(View.GONE);

            if (Status.SUCCESS == result.status) {
                mSwipeRefresh.setEnabled(false);
                mSwipeRefresh.setRefreshing(false);

                mHomeTabAdapter.setTabData(result.data);
                mHomeTabAdapter.notifyDataSetChanged();
            }

            if (Status.LOADING == result.status) {
                mSwipeRefresh.setEnabled(true);
                mSwipeRefresh.setRefreshing(true);
            }

            if (Status.ERROR == result.status) {
                mSwipeRefresh.setRefreshing(false);

                if (result.data != null && result.data.size() != 0) {
                    mSwipeRefresh.setEnabled(false);
                    mHomeTabAdapter.setTabData(result.data);
                    mHomeTabAdapter.notifyDataSetChanged();

                } else {
                    mSwipeRefresh.setEnabled(true);
                    mLoadError.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @BindView(R.id.home_swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.home_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.home_view_pager)
    ViewPager mViewPager;

    @BindView(R.id.home_error)
    View mLoadError;

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page, container, false);
    }

    @Override
    protected void doViewCreate(View view, Bundle savedInstanceState) {
        super.doViewCreate(view, savedInstanceState);

        mHomeViewModel = ViewModelProviders.of(this, mViewModelFactory).get(HomePageViewModel.class);

        mHomeViewModel.getHomeArea().observe(this, mHomeResourceObserver);

        mHomeTabAdapter = new HomeTabAdapter(getFragmentManager());
        mViewPager.setAdapter(mHomeTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void doResume() {
        super.doResume();
        mSwipeRefresh.setOnRefreshListener(mRefreshListener);
    }

    @Override
    protected void doPause() {
        super.doPause();
        mSwipeRefresh.setOnRefreshListener(null);
    }

    @VisibleForTesting
    public static class HomeTabAdapter extends FragmentPagerAdapter {

        private List<HomeArea> mTabData = new ArrayList<>();

        HomeTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTabData.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabData.get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return HomeChildFragment.newInstance(mTabData.get(position));
        }

        void setTabData(List<HomeArea> tabData) {
            mTabData.clear();
            mTabData.addAll(tabData);
        }
    }
}
