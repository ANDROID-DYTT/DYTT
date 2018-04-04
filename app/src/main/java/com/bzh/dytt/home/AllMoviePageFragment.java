package com.bzh.dytt.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.MovieCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AllMoviePageFragment extends BaseFragment {

    private static final String TAG = "AllMoviePageFragment";
    MovieTabAdapter mMovieTabAdapter;
    @BindView(R.id.home_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.home_view_pager)
    ViewPager mViewPager;

    public static AllMoviePageFragment newInstance() {
        return new AllMoviePageFragment();
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page, container, false);
    }

    @Override
    protected void doViewCreated(View view, Bundle savedInstanceState) {
        super.doViewCreated(view, savedInstanceState);

        mMovieTabAdapter = new MovieTabAdapter(getFragmentManager());
        mViewPager.setAdapter(mMovieTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        List<MovieCategory> typeList = new ArrayList<>();
        typeList.add(MovieCategory.NEW_MOVIE);
        typeList.add(MovieCategory.CHINA_MOVIE);
        typeList.add(MovieCategory.OUMEI_MOVIE);
        typeList.add(MovieCategory.RIHAN_MOVIE);

        mMovieTabAdapter.setTabData(typeList);
    }

    @VisibleForTesting
    public static class MovieTabAdapter extends FragmentStatePagerAdapter {

        private List<MovieCategory> mTabData = new ArrayList<>();

        MovieTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTabData.size();
        }


        @Override
        public Fragment getItem(int position) {
            return LoadableMoviePageFragment.newInstance(mTabData.get(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabData.get(position).getTitle();
        }

        void setTabData(List<MovieCategory> tabData) {
            mTabData.clear();
            mTabData.addAll(tabData);
            notifyDataSetChanged();
        }
    }
}
