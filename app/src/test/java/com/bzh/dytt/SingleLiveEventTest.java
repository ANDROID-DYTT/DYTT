package com.bzh.dytt;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SingleLiveEventTest {

    // Execute tasks synchronously
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    // The class that has the lifecycle
    @Mock
    private LifecycleOwner mOwner;

    // The observer of the event under test
    @Mock
    private Observer<Integer> mEventObserver;

    // Defines the Android Lifecycle of an object, used to trigger different events
    private LifecycleRegistry mLifecycle;

    // Event object under test
    private SingleLiveEvent<Integer> mSingleLiveEvent = new SingleLiveEvent<>();

    @Before
    public void setUpLifecycles() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Link custom lifecycle owner with the lifecyle register.
        mLifecycle = new LifecycleRegistry(mOwner);
        when(mOwner.getLifecycle()).thenReturn(mLifecycle);

        // Start observing
        mSingleLiveEvent.observe(mOwner, mEventObserver);

        // Start in a non-active state
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @Test
    public void valueNotSet_onFirstOnResume() {
        // On resume
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        // no updateItem should be emitted because no value has been set
        verify(mEventObserver, never()).onChanged(anyInt());
    }

    @Test
    public void singleUpdate_onSecondOnResume_updatesOnce() {
        // After a value is set
        mSingleLiveEvent.setValue(42);

        // observers are called once on resume
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        // on second resume, no updateItem should be emitted.
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        // Check that the observer is called once
        verify(mEventObserver, times(1)).onChanged(anyInt());
    }

    @Test
    public void twoUpdates_updatesTwice() {
        // After a value is set
        mSingleLiveEvent.setValue(42);

        // observers are called once on resume
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        // when the value is set again, observers are called again.
        mSingleLiveEvent.setValue(23);

        // Check that the observer has been called twice
        verify(mEventObserver, times(2)).onChanged(anyInt());
    }

    @Test
    public void twoUpdates_noUpdateUntilActive() {
        // Set a value
        mSingleLiveEvent.setValue(42);

        // which doesn't emit a change
        verify(mEventObserver, never()).onChanged(42);

        // and set it again
        mSingleLiveEvent.setValue(42);

        // observers are called once on resume.
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        // Check that the observer is called only once
        verify(mEventObserver, times(1)).onChanged(anyInt());
    }
}
