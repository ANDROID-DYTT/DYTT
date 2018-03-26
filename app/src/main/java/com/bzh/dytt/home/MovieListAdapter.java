package com.bzh.dytt.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bzh.dytt.R;
import com.bzh.dytt.SingleActivity;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.util.GlideApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemHolder> {

    private List<VideoDetail> mItems = new ArrayList<>();
    private Context mContext;

    public MovieListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_child, parent, false);
        return new MovieListAdapter.MovieItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieListAdapter.MovieItemHolder holder, int position) {
        final VideoDetail videoDetail = mItems.get(position);

        if (videoDetail != null) {
            holder.VideoTitleTv.setText((!TextUtils.isEmpty(videoDetail.getCountry()) && videoDetail.getCountry()
                    .contains("中国")) ? videoDetail.getName() :
                    videoDetail.getTranslationName());
            holder.VideoPublishTv.setText(videoDetail.getPublishTime());
            if (!TextUtils.isEmpty(videoDetail.getDoubanGrade())) {
                holder.DoubanGradeTv.setText("豆瓣/" + videoDetail.getDoubanGrade());
            }
            if (!TextUtils.isEmpty(videoDetail.getIMDBGrade())) {
                holder.IMDBGradeTv.setText("IMDB/" + videoDetail.getIMDBGrade());
            }
            holder.VideoDescriptionTv.setText(videoDetail.getDescription());

            GlideApp.with(mContext)
                    .load(videoDetail.getCoverUrl())
                    .placeholder(R.drawable.default_video)
                    .into(holder.VideoCover);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = getActivityByHolder(holder);
                    if (activity != null) {
                        Intent intent = new Intent(activity, SingleActivity.class);
                        intent.putExtra("DETAIL_LINK", videoDetail.getDetailLink());
                        activity.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    void setItems(List<VideoDetail> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    private Activity getActivityByHolder(MovieListAdapter.MovieItemHolder holder) {
        if (holder.itemView.getContext() instanceof Activity) {
            return ((Activity) holder.itemView.getContext());
        }
        return null;
    }

    public class MovieItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_title)
        TextView VideoTitleTv;

        @BindView(R.id.video_publish_time)
        TextView VideoPublishTv;

        @BindView(R.id.douban_grade)
        TextView DoubanGradeTv;

        @BindView(R.id.imdb_grade)
        TextView IMDBGradeTv;

        @BindView(R.id.video_description)
        TextView VideoDescriptionTv;

        @BindView(R.id.video_cover)
        ImageView VideoCover;

        MovieItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VideoDetailDiffCallback extends DiffUtil.Callback {

        List<VideoDetail> oldVideoDetails;
        List<VideoDetail> newVideoDetails;

        public VideoDetailDiffCallback(List<VideoDetail> newVideoDetails, List<VideoDetail> oldVideoDetails) {
            this.newVideoDetails = newVideoDetails;
            this.oldVideoDetails = oldVideoDetails;
        }

        @Override
        public int getOldListSize() {
            return oldVideoDetails.size();
        }

        @Override
        public int getNewListSize() {
            return newVideoDetails.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return TextUtils.equals(oldVideoDetails.get(oldItemPosition).getDetailLink(), newVideoDetails.get
                    (newItemPosition).getDetailLink());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return TextUtils.equals(oldVideoDetails.get(oldItemPosition).getName(), newVideoDetails.get
                    (newItemPosition).getName());
        }

    }
}
