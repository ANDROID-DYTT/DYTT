package com.bzh.dytt.home;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bzh.dytt.R;
import com.bzh.dytt.SingleActivity;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.util.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemHolder> {

    private List<VideoDetail> mItems;
    private Context mContext;
    private int mDataVersion = 0;

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
                        SingleActivity.startDetailPage(activity, videoDetail.getDetailLink());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(final List<VideoDetail> update) {
        mDataVersion++;
        if (mItems == null) {
            if (update == null) {
                return;
            }
            mItems = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = mItems.size();
            mItems = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = mDataVersion;
            final List<VideoDetail> oldItems = mItems;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            VideoDetail oldItem = oldItems.get(oldItemPosition);
                            VideoDetail newItem = update.get(newItemPosition);
                            return TextUtils.equals(oldItem.getDetailLink(), newItem.getDetailLink());
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            VideoDetail oldItem = oldItems.get(oldItemPosition);
                            VideoDetail newItem = update.get(newItemPosition);
                            return TextUtils.equals(oldItem.getName(), newItem.getName());
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != mDataVersion) {
                        // ignore update
                        return;
                    }
                    mItems = update;
                    diffResult.dispatchUpdatesTo(MovieListAdapter.this);

                }
            }.execute();
        }
    }


    private Activity getActivityByHolder(MovieListAdapter.MovieItemHolder holder) {
        if (holder.itemView.getContext() instanceof Activity) {
            return ((Activity) holder.itemView.getContext());
        }
        return null;
    }

    class MovieItemHolder extends RecyclerView.ViewHolder {

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
}
