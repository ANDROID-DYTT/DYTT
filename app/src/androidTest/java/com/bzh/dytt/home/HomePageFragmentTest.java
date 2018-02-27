package com.bzh.dytt.home;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.testing.SingleFragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class HomePageFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mActivityTestRule = new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class, true, true);
    private HomePageFragment mHomePageFragment;
    private HomePageViewModel mHomePageViewModel;

    @Before
    public void init(){

        mHomePageFragment = HomePageFragment.newInstance();
        mHomePageViewModel = mock(HomePageViewModel.class);
    }

    @Test
    public void testTabLayoutClick() {

        onView(withText("欧美电视剧")).perform(click());

        onView(withText("2018新片精品")).perform(click());
    }

}