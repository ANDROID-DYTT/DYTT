package com.bzh.dytt.search;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bzh.dytt.R;
import com.bzh.dytt.data.VideoDetail;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.home.MovieListAdapter;
import com.bzh.dytt.home.SingleListFragment;

import java.util.List;

import javax.inject.Inject;

public class SearchFragment extends SingleListFragment<VideoDetail> {

    private static final String TAG = "SearchFragment";

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    ViewModelProvider.Factory mFactory;
    private EditText mSearchInput;
    private TextView.OnEditorActionListener mSearchActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (getActivity() != null && !TextUtils.isEmpty(v.getText())) {
                    String searchTarget = v.getText().toString().trim();
                    ((SearchViewModel) mViewModel).setQuery(searchTarget);
                    mSwipeRefresh.setEnabled(true);
                    mSwipeRefresh.setRefreshing(true);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(mSearchInput.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
                return true;
            }
            return false;
        }
    };

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void doCreate(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null && getActivity().getWindow() != null) {
            int softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
            getActivity().getWindow().setSoftInputMode(softInputMode);
        }
        super.doCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                View customNav = LayoutInflater.from(getActivity()).inflate(R.layout.search_action_bar, null); // layout which contains your button.
                actionBar.setCustomView(customNav, lp1);
                mSearchInput = customNav.findViewById(R.id.search_edit_input);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchInput.setOnEditorActionListener(mSearchActionListener);
        mSwipeRefresh.setEnabled(false);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    protected RecyclerView.Adapter createAdapter() {
        return new MovieListAdapter(this.getContext());
    }

    @Override
    protected void replace(List<VideoDetail> listData) {
        ((MovieListAdapter) mAdapter).replace(listData);
    }

    @Override
    protected LiveData<Resource<List<VideoDetail>>> getListLiveData() {
        return ((SearchViewModel) mViewModel).getVideoList();
    }

    @Override
    protected LiveData<Throwable> getThrowableLiveData() {
        return ((SearchViewModel) mViewModel).getFetchVideoDetailState();
    }

    @Override
    public Observer<Resource<List<VideoDetail>>> getListObserver() {
        return new Observer<Resource<List<VideoDetail>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<VideoDetail>> result) {
                mListObserver.onChanged(result);
                assert result != null;
                switch (result.status) {
                    case ERROR:
                    case SUCCESS:
                        mSwipeRefresh.setEnabled(false);
                        mSwipeRefresh.setRefreshing(false);
                        break;
                }
            }
        };
    }

    @Override
    protected ViewModel createViewModel() {
        return ViewModelProviders.of(this, mFactory).get(SearchViewModel.class);
    }
}
