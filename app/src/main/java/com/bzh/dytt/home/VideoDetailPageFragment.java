package com.bzh.dytt.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.source.Resource;
import com.bzh.dytt.data.source.Status;

import javax.inject.Inject;

import butterknife.BindView;

public class VideoDetailPageFragment extends BaseFragment{

    public static VideoDetailPageFragment newInstance(String detailLink) {
        Bundle args = new Bundle();
        args.putString("DETAIL_LINK", detailLink);
        VideoDetailPageFragment fragment = new VideoDetailPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final String TAG = "VideoDetailPageFragment";

    private String mDetailLink;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private VideoDetailPageViewModel mVideoDetailPageViewModel;


    @Override
    protected void doCreate(@Nullable Bundle savedInstanceState) {
        super.doCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mDetailLink = arguments.getString("DETAIL_LINK");
    }

    @BindView(R.id.video_name)
    TextView mVideoNameTv;

    @BindView(R.id.video_type)
    TextView mVideoTypeTv;

    @BindView(R.id.video_country)
    TextView mVideoCountry;

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_detail_layout, container, false);
    }

    private Observer<Resource<VideoDetail>> mVideoDetailObserver = new Observer<Resource<VideoDetail>>() {

        @Override
        public void onChanged(@Nullable Resource<VideoDetail> videoDetailResource) {
            VideoDetail videoDetail;
            if(videoDetailResource.status == Status.SUCCESS) {
                videoDetail = videoDetailResource.data;
                if(videoDetail == null) {
                    return;
                }
                mVideoNameTv.setText(videoDetail.getName());
                mVideoTypeTv.setText(videoDetail.getCategory());
                mVideoCountry.setText(videoDetail.getCountry());
            }
        }
    };

    @Override
    protected void doViewCreate(View view, Bundle savedInstanceState) {
        super.doViewCreate(view, savedInstanceState);
        mVideoDetailPageViewModel = ViewModelProviders.of(this, mViewModelFactory).get(VideoDetailPageViewModel.class);
        mVideoDetailPageViewModel.getVideoDetail(mDetailLink).observe(this, mVideoDetailObserver);

    }
}
