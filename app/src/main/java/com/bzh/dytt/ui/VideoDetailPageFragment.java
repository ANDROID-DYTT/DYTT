package com.bzh.dytt.ui;


import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bzh.dytt.BaseFragment;
import com.bzh.dytt.R;
import com.bzh.dytt.data.Resource;
import com.bzh.dytt.data.Status;
import com.bzh.dytt.data.entity.VideoDetail;
import com.bzh.dytt.util.GlideApp;
import com.bzh.dytt.util.ThunderHelper;
import com.bzh.dytt.viewmodel.VideoDetailPageViewModel;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

public class VideoDetailPageFragment extends BaseFragment {

    private static final String TAG = "VideoDetailPageFragment";
    private static final String KEY_DETAIL_LINK = "DETAIL_LINK";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    ImageView mVideoCoverIv;

    TextView mVideoNameTv;

    TextView mVideoTypeTv;

    TextView mVideoCountryTv;

    TextView mVideoDuration;

    TextView mVideoShowTime;

    TextView mVideoDirector;

    TextView mVideoLeadingRole;

    TextView mVideoDescription;

    View mVideoCoverBgView;

    View mDownloadBtn;

    TextView mDoubanGradeTv;

    RatingBar mDoubanGradeRatingBar;

    TextView mIMDBGradeTv;

    RatingBar mIMDBRatingBar;

    View mDoubanRatingLayout;

    View mIMDBRatingLayout;

    View mShowTimeLayout;

    AdView mAdView;

    private String mDetailLink;

    private VideoDetailPageViewModel mVideoDetailPageViewModel;
    private ThunderHelper mThunderHelper;
    private Observer<Resource<List<VideoDetail>>> mVideoDetailObserver = new Observer<Resource<List<VideoDetail>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<VideoDetail>> result) {
            if (result == null) {
                return;
            }
            final VideoDetail videoDetail;
            if (result.status == Status.SUCCESS && result.data != null) {
                videoDetail = result.data.get(0);
                if (videoDetail == null) {
                    return;
                }

                boolean isNotChina = !TextUtils.isEmpty(videoDetail.getCountry())
                        && videoDetail.getCountry().contains(getString(R.string.china));
                String videoName = isNotChina ? videoDetail.getName() : videoDetail.getTranslationName();

                if (getActivity() != null) {
                    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(videoName);
                    }
                }
                mVideoTypeTv.setText(videoDetail.getType());
                mVideoCountryTv.setText(videoDetail.getCountry());
                mVideoDuration.setText(videoDetail.getDuration());
                mVideoShowTime.setText(videoDetail.getShowTime());
                mVideoTypeTv.setText(videoDetail.getType());
                mVideoNameTv.setText(videoName);

                if (TextUtils.isEmpty(videoDetail.getDoubanGrade()) || Objects.equals(videoDetail.getDoubanGrade(), "0")) {
                    mDoubanRatingLayout.setVisibility(View.GONE);
                } else {
                    mDoubanGradeTv.setText(getString(R.string.douban_grade, videoDetail.getDoubanGrade()));
                    mDoubanGradeRatingBar.setRating(Float.parseFloat(videoDetail.getDoubanGrade()) / 2);
                }

                if (TextUtils.isEmpty(videoDetail.getIMDBGrade()) || Objects.equals(videoDetail.getIMDBGrade(), "0")) {
                    mIMDBRatingLayout.setVisibility(View.GONE);
                } else {
                    mIMDBGradeTv.setText(getString(R.string.imdb_grade, videoDetail.getIMDBGrade()));
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
                        if (getActivity() == null || !mThunderHelper.onClickDownload(getActivity(), videoDetail.getDownloadLink())) {
                            InnerDialogFragment dialogFragment = new InnerDialogFragment();
                            dialogFragment.show(getActivity().getSupportFragmentManager(), "InnerDialog");
                        }
                    }
                });

                if (TextUtils.isEmpty(videoDetail.getShowTime())) {
                    mShowTimeLayout.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(videoDetail.getCoverUrl())) {

                    GlidePalette<Drawable> glidePalette = GlidePalette.with(videoDetail.getCoverUrl())
                            .use(GlidePalette.Profile.MUTED_DARK)
                            .intoBackground(mVideoCoverBgView)
                            .crossfade(true)
                            .intoCallBack(new BitmapPalette.CallBack() {
                                @Override
                                public void onPaletteLoaded(@Nullable Palette palette) {
                                    if (palette != null && getActivity() != null) {
                                        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                        if (actionBar != null) {
                                            actionBar.setBackgroundDrawable(new ColorDrawable(palette.getDarkMutedColor(Color.WHITE)));
                                        }
                                    }
                                }
                            });

                    GlideApp.with(VideoDetailPageFragment.this)
                            .load(videoDetail.getCoverUrl())
                            .listener(glidePalette)
                            .placeholder(R.drawable.default_video)
                            .into(mVideoCoverIv);
                }
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
        if (arguments != null) {
            mDetailLink = arguments.getString(KEY_DETAIL_LINK);
        }
        mThunderHelper = new ThunderHelper();
    }

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_detail_layout, container, false);
    }

    @Override
    protected void doViewCreated(View view, Bundle savedInstanceState) {
        super.doViewCreated(view, savedInstanceState);

        mVideoCoverIv = view.findViewById(R.id.video_cover);
        mVideoNameTv = view.findViewById(R.id.video_name);
        mVideoTypeTv = view.findViewById(R.id.video_type);
        mVideoCountryTv = view.findViewById(R.id.video_country);
        mVideoDuration = view.findViewById(R.id.video_duration);
        mVideoShowTime = view.findViewById(R.id.video_show_time);
        mVideoDirector = view.findViewById(R.id.video_director);
        mVideoLeadingRole = view.findViewById(R.id.video_leading_role);
        mVideoDescription = view.findViewById(R.id.video_description);
        mVideoCoverBgView = view.findViewById(R.id.video_cover_bg);
        mDownloadBtn = view.findViewById(R.id.download_button);
        mDoubanGradeTv = view.findViewById(R.id.douban_grade);
        mDoubanGradeRatingBar = view.findViewById(R.id.douban_rating_bar);
        mIMDBGradeTv = view.findViewById(R.id.imdb_grade);
        mIMDBRatingBar = view.findViewById(R.id.imdb_rating_bar);
        mDoubanRatingLayout = view.findViewById(R.id.douban_rating_layout);
        mIMDBRatingLayout = view.findViewById(R.id.imdb_rating_layout);
        mShowTimeLayout = view.findViewById(R.id.show_time_layout);
        mAdView = view.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Map<String, String> map = new HashMap<>();
        map.put("link", mDetailLink);
        MobclickAgent.onEvent(getActivity(), "video_detail", map);

        mVideoDetailPageViewModel = ViewModelProviders.of(this, mViewModelFactory).get(VideoDetailPageViewModel.class);
        if (!TextUtils.isEmpty(mDetailLink)) {
            mVideoDetailPageViewModel.getVideoDetail(mDetailLink).observe(this, mVideoDetailObserver);
        } else {
            Logger.e(TAG, "doViewCreated: Detail Link Is Empty");
        }
    }

    public static class InnerDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.un_install_xunlei_label).setPositiveButton(R.string.ok, null);
            return builder.create();
        }
    }

}
