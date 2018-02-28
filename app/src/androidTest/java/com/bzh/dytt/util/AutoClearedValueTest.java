package com.bzh.dytt.util;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.bzh.dytt.testing.SingleFragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
public class AutoClearedValueTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private TestFragment testFragment;

    @Before
    public void init() {
        testFragment = new TestFragment();
        activityRule.getActivity().setFragment(testFragment);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @Test
    public void clearOnReplace() throws Throwable {
        testFragment.testValue = new AutoClearedValue<>(testFragment, "foo");
        activityRule.getActivity().replaceFragment(new TestFragment());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(testFragment.testValue.get(), nullValue());
    }

    @Test
    public void dontClearForChildFragment() throws Throwable {
        testFragment.testValue = new AutoClearedValue<>(testFragment, "foo");
        testFragment.getChildFragmentManager().beginTransaction()
                .add(new Fragment(), "foo").commit();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(testFragment.testValue.get(), is("foo"));
    }

    @Test
    public void dontClearForDialog() throws Throwable {
        testFragment.testValue = new AutoClearedValue<>(testFragment, "foo");
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(testFragment.getFragmentManager(), "dialog");
        dialogFragment.dismiss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(testFragment.testValue.get(), is("foo"));
    }

    public static class TestFragment extends Fragment {

        AutoClearedValue<String> testValue;
    }
}