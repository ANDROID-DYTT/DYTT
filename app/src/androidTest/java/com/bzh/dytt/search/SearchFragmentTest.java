package com.bzh.dytt.search;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.testing.SingleFragmentActivity;
import com.bzh.dytt.util.EspressoTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    @Rule
    ActivityTestRule<SingleFragmentActivity> mActivityTestRule = new ActivityTestRule<>(SingleFragmentActivity.class);

    private SearchFragment mFragment;

    @Before
    public void setUp() throws Exception {
        EspressoTestUtil.disableProgressBarAnimations(mActivityTestRule);
        mFragment = SearchFragment.newInstance();
        mActivityTestRule.getActivity().setFragment(mFragment);
    }
}