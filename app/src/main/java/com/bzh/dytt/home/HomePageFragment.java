package com.bzh.dytt.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.source.Resource;
import com.bzh.dytt.data.source.Status;
import com.bzh.dytt.util.ViewModelFactory;

import java.util.List;

public class HomePageFragment extends BaseFragment {

    private static final String TAG = "HomePageFragment";

    private HomePageViewModel mHomeViewModel;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    protected void doCreate(@Nullable Bundle savedInstanceState) {
        super.doCreate(savedInstanceState);


        mHomeViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(getActivity().getApplication())).get(HomePageViewModel.class);

        mHomeViewModel.getHomeArea().observe(this, new Observer<Resource<List<HomeArea>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<HomeArea>> result) {
                if (Status.SUCCESS == result.status) {


                    for (HomeArea area :
                            result.data) {
                        Log.d(TAG, "onChanged: SUCCESS " + area.getTitle());
                    }
                }
                if (Status.LOADING == result.status) {
                    Log.d(TAG, "onChanged: LOADING");
                }
                if (Status.ERROR == result.status) {
                    Log.d(TAG, "onChanged: ERROR");
                }
            }
        });
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page, container, false);
    }

    @Override
    protected void doResume() {
        super.doResume();
    }

}
