package com.bzh.dytt.home;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bzh.dytt.R;
import com.bzh.dytt.data.HomeArea;
import com.bzh.dytt.data.HomeItem;
import com.bzh.dytt.data.HomeType;
import com.bzh.dytt.data.network.Resource;
import com.bzh.dytt.testing.SingleFragmentActivity;
import com.bzh.dytt.util.EspressoTestUtil;
import com.bzh.dytt.util.RecyclerViewMatcher;
import com.bzh.dytt.util.ViewModelUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HomeChildFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mTestRule = new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class);


    private HomeArea mHomeArea;

    private MutableLiveData<Resource<List<HomeItem>>> mHomeItems = new MutableLiveData<>();

    @Before
    public void init() throws Exception {
        mHomeArea = new HomeArea();
        mHomeArea.setTitle("2018新片精品");
        mHomeArea.setType(HomeType.NEWEST);
        mHomeArea.setDetailLink("/html/gndy/dyzz/index.html");

        EspressoTestUtil.disableProgressBarAnimations(mTestRule);

    }

    @Test
    public void refresh() {
        onView(withId(R.id.home_child_swipe_refresh)).perform(swipeDown());

        List<HomeItem> homeItems = setHomeItems(2);
        mHomeItems.postValue(Resource.success(homeItems));
        onView(withId(R.id.home_child_empty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.home_child_error)).check(matches(not(isDisplayed())));

        for (int pos = 0; pos < homeItems.size(); pos++) {
            HomeItem homeItem = homeItems.get(pos);
            onView(listMatcher().atPosition(pos)).check(matches(hasDescendant(withText(homeItem.getTitle()))));
            onView(listMatcher().atPosition(pos)).check(matches(hasDescendant(withText(homeItem.getTime()))));
        }
    }

    @Test
    public void loading() {
//        mHomeItems.postValue(Resource.loading(null));
        onView(withId(R.id.home_child_swipe_refresh)).check(matches(isDisplayed()));
    }

    @Test
    public void error() {
//        mHomeItems.postValue(Resource.error("wft", null));
        onView(withId(R.id.home_child_error)).check(matches(isDisplayed()));
    }

    @Test
    public void successEmptyData() {
//        mHomeItems.postValue(Resource.success(null));
        onView(withId(R.id.home_child_empty)).check(matches(isDisplayed()));
        onView(listMatcher().atPosition(0)).check(doesNotExist());
    }

    @Test
    public void successHaveData() {
        List<HomeItem> homeItems = setHomeItems(2);
        mHomeItems.postValue(Resource.success(homeItems));
        onView(withId(R.id.home_child_empty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.home_child_error)).check(matches(not(isDisplayed())));

        for (int pos = 0; pos < homeItems.size(); pos++) {
            HomeItem homeItem = homeItems.get(pos);
            onView(listMatcher().atPosition(pos)).check(matches(hasDescendant(withText(homeItem.getTitle()))));
            onView(listMatcher().atPosition(pos)).check(matches(hasDescendant(withText(homeItem.getTime()))));
        }
    }

    @NonNull
    private List<HomeItem> setHomeItems(int count) {
        List<HomeItem> homeItems = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            homeItems.add(new HomeItem("电影-" + i, "2018-03-01", "/html/gndy/dyzz/20180301/" + i + ".html;", HomeType.NEWEST));
        }
        return homeItems;
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.home_child_recycler_view);
    }

}