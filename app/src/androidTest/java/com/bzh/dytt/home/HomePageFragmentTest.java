package com.bzh.dytt.home;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.R;
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

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HomePageFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mActivityTestRule = new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class, true, true);
    private HomePageFragment mHomePageFragment;
    private HomePageViewModel mHomePageViewModel;


    private MutableLiveData<Resource<List<HomeArea>>> mHomeAreas = new MutableLiveData<>();

    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(mActivityTestRule);

        mHomePageFragment = HomePageFragment.newInstance();
        mHomePageViewModel = mock(HomePageViewModel.class);

        when(mHomePageViewModel.getHomeArea()).thenReturn(mHomeAreas);

        mHomePageFragment.mViewModelFactory = ViewModelUtil.createFor(mHomePageViewModel);

        mActivityTestRule.getActivity().setFragment(mHomePageFragment);
    }

    @Test
    public void success() {
        HomeArea homeArea = new HomeArea("别志华", HomeType.NEWEST_168);
        HomeArea homeArea2 = new HomeArea("胡玉琼", HomeType.NEWEST);
        List<HomeArea> list = new ArrayList<>();
        list.add(homeArea);
        list.add(homeArea2);
        mHomeAreas.postValue(Resource.success(list));
        onView(withId(R.id.home_swipe_refresh)).check(matches(not(isEnabled())));
        onView(withId(R.id.home_error)).check(matches(not(isDisplayed())));
        onView(withText(homeArea.getTitle())).check(matches(isDisplayed()));
        onView(withText(homeArea2.getTitle())).check(matches(isDisplayed()));

        onView(withText(homeArea.getTitle())).perform(click());
        onView(withText(homeArea.getTitle())).check(matches(withText(mHomePageFragment.mHomeTabAdapter.getPageTitle(0).toString())));

        onView(withText(homeArea2.getTitle())).perform(click());
        onView(withText(homeArea2.getTitle())).check(matches(withText(mHomePageFragment.mHomeTabAdapter.getPageTitle(1).toString())));

    }

    @Test
    public void loading() {
        mHomeAreas.postValue(Resource.loading(null));
        onView(withId(R.id.home_swipe_refresh)).check(matches(isDisplayed()));
    }

    @Test
    public void errorNullData() {
        mHomeAreas.postValue(Resource.error("wtf", null));
        onView(withId(R.id.home_swipe_refresh)).check(matches(isEnabled()));
        onView(withId(R.id.home_error)).check(matches(isDisplayed()));
    }

    @Test
    public void errorHaveData() {
        List<HomeArea> list = new ArrayList<>();
        HomeArea homeArea = new HomeArea("别志华", HomeType.NEWEST_168);
        list.add(homeArea);
        mHomeAreas.postValue(Resource.error("wtf", list));
        onView(withId(R.id.home_swipe_refresh)).check(matches(not(isEnabled())));
        onView(withId(R.id.home_error)).check(matches(not(isDisplayed())));
        onView(withText(homeArea.getTitle())).check(matches(isDisplayed()));
    }
}