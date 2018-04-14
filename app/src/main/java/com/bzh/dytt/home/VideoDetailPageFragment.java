package com.bzh.dytt.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.data.network.Status;
import com.bzh.dytt.util.GlideApp;
import com.bzh.dytt.util.ThunderHelper;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;

public class VideoDetailPageFragment extends BaseFragment {

    private static final String TAG = "VideoDetailPageFragment";
    private static final String KEY_DETAIL_LINK = "DETAIL_LINK";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @BindView(R.id.video_cover)
    ImageView mVideoCoverIv;

    @BindView(R.id.video_name)
    TextView mVideoNameTv;

    @BindView(R.id.video_type)
    TextView mVideoTypeTv;

    @BindView(R.id.video_country)
    TextView mVideoCountryTv;

    @BindView(R.id.video_duration)
    TextView mVideoDuration;

    @BindView(R.id.video_show_time)
    TextView mVideoShowTime;

    @BindView(R.id.video_director)
    TextView mVideoDirector;

    @BindView(R.id.video_leading_role)
    TextView mVideoLeadingRole;

    @BindView(R.id.video_description)
    TextView mVideoDescription;

    @BindView(R.id.video_cover_bg)
    View mVideoCoverBgView;

    @BindView(R.id.download_button)
    View mDownloadBtn;

    @BindView(R.id.douban_grade)
    TextView mDoubanGradeTv;

    @BindView(R.id.douban_rating_bar)
    RatingBar mDoubanGradeRatingBar;

    @BindView(R.id.imdb_grade)
    TextView mIMDBGradeTv;

    @BindView(R.id.imdb_rating_bar)
    RatingBar mIMDBRatingBar;

    @BindView(R.id.douban_rating_layout)
    View mDoubanRatingLayout;

    @BindView(R.id.imdb_rating_layout)
    View mIMDBRatingLayout;

    @BindView(R.id.show_time_layout)
    View mShowTimeLayout;

    @BindView(R.id.adView)
    AdView mAdView;

    private String mDetailLink;

    private VideoDetailPageViewModel mVideoDetailPageViewModel;

    private Observer<Resource<List<VideoDetail>>> mVideoDetailObserver = new Observer<Resource<List<VideoDetail>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<VideoDetail>> result) {
            final VideoDetail videoDetail;
            if (result.status == Status.SUCCESS && !result.data.isEmpty()) {
                videoDetail = result.data.get(0);
                if (videoDetail == null) {
                    return;
                }
                String videoName = (!TextUtils.isEmpty(videoDetail.getCountry()) && videoDetail.getCountry()
                        .contains("中国")) ? videoDetail.getName() :
                        videoDetail.getTranslationName();

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(videoName);
                mVideoTypeTv.setText(videoDetail.getType());
                mVideoCountryTv.setText(videoDetail.getCountry());
                mVideoDuration.setText(videoDetail.getDuration());
                mVideoShowTime.setText(videoDetail.getShowTime());
                mVideoTypeTv.setText(videoDetail.getType());

                mVideoNameTv.setText(videoName);

                if (TextUtils.isEmpty(videoDetail.getDoubanGrade()) || Objects.equals(videoDetail.getDoubanGrade(), "0")) {
                    mDoubanRatingLayout.setVisibility(View.GONE);
                } else {
                    mDoubanGradeTv.setText("豆瓣/" + videoDetail.getDoubanGrade());
                    mDoubanGradeRatingBar.setRating(Float.parseFloat(videoDetail.getDoubanGrade()) / 2);
                }

                if (TextUtils.isEmpty(videoDetail.getIMDBGrade()) || Objects.equals(videoDetail.getIMDBGrade(), "0")) {
                    mIMDBRatingLayout.setVisibility(View.GONE);
                } else {
                    mIMDBGradeTv.setText("IMDB/" + videoDetail.getIMDBGrade());
                    mIMDBRatingBar.setRating(Float.parseFloat(videoDetail.getIMDBGrade()) / 2);
                }

                StringBuilder directorStrBuilder = new StringBuilder();
                for (String director : videoDetail.getDirector()) {
                    directorStrBuilder.append(director);
                    directorStrBuilder.append(" ");
                }
                mVideoDirector.setText(directorStrBuilder.toString());

                StringBuilder leadingRoleStrBuilder = new StringBuilder();
                for (String director : videoDetail.getLeadingRole()) {
                    leadingRoleStrBuilder.append(director);
                    leadingRoleStrBuilder.append(" ");
                }
                mVideoLeadingRole.setText(leadingRoleStrBuilder.toString());
                mVideoDescription.setText(videoDetail.getDescription());
                mVideoDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((TextView) view).getMaxLines() > 4) {
                            ((TextView) view).setMaxLines(4);
                        } else {
                            ((TextView) view).setMaxLines(100);
                        }
                    }
                });

                mDownloadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ThunderHelper.getInstance(getActivity()).onClickDownload(videoDetail.getDownloadLink());
                    }
                });

                if (TextUtils.isEmpty(videoDetail.getShowTime())) {
                    mShowTimeLayout.setVisibility(View.GONE);
                }

                GlideApp.with(VideoDetailPageFragment.this)
                        .load(videoDetail.getCoverUrl())
                        .listener(GlidePalette.with(videoDetail.getCoverUrl()).use(GlidePalette.Profile.MUTED_DARK)
                                .intoBackground(mVideoCoverBgView).crossfade(true).intoCallBack(new BitmapPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(@Nullable Palette palette) {
                                        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getDarkMutedColor(Color.WHITE)));
                                    }
                                }))
                        .placeholder(R.drawable.default_video)
                        .into(mVideoCoverIv);
            }
        }
    };

    public static VideoDetailPageFragment newInstance(String detailLink) {
        Bundle args = new Bundle();
        args.putString(KEY_DETAIL_LINK, detailLink);
        VideoDetailPageFragment fragment = new VideoDetailPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void doCreate(@Nullable Bundle savedInstanceState) {
        super.doCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mDetailLink = arguments.getString(KEY_DETAIL_LINK);
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_detail_layout, container, false);
    }

    @Override
    protected void doViewCreated(View view, Bundle savedInstanceState) {
        super.doViewCreated(view, savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Map<String, String> map = new HashMap<>();
        map.put("link", mDetailLink);
        MobclickAgent.onEvent(getActivity(), "video_detail", map);

        mVideoDetailPageViewModel = ViewModelProviders.of(this, mViewModelFactory).get(VideoDetailPageViewModel.class);
        if (!TextUtils.isEmpty(mDetailLink)) {

            mVideoDetailPageViewModel.getVideoDetail(mDetailLink).observe(this, mVideoDetailObserver);
        } else {
            Log.e(TAG, "doViewCreated: Detail Link Is Empty");
        }

    }
}
