package com.bzh.dytt.home;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.testing.SingleFragmentActivity;
import com.bzh.dytt.util.EspressoTestUtil;
import com.bzh.dytt.util.ViewModelUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HomePageFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mActivityTestRule = new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class, true, true);
    private HomePageFragment mHomePageFragment;
    private HomePageViewModel mHomePageViewModel;


    private MutableLiveData<Resource<List<HomeArea>>> mHomeAreas = new MutableLiveData<>();
    private MutableLiveData<Resource<List<HomeItem>>> mHomeItems = new MutableLiveData<>();

    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(mActivityTestRule);

        mHomePageFragment = HomePageFragment.newInstance();
        mHomePageViewModel = mock(HomePageViewModel.class);

        when(mHomePageViewModel.getHomeArea()).thenReturn(mHomeAreas);
        when(mHomePageViewModel.getHomeItems(HomeType.NEWEST_168)).thenReturn(mHomeItems);

        mHomePageFragment.mViewModelFactory = ViewModelUtil.createFor(mHomePageViewModel);

        mActivityTestRule.getActivity().setFragment(mHomePageFragment);
    }

    @Test
    public void testTabLayoutClick() {

//        onView(withText("欧美电视剧")).perform(click());
//
//        onView(withText("2018新片精品")).perform(click());
    }

}