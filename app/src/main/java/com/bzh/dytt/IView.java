package com.bzh.dytt;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;

public interface IView<T> {

    <VM extends ViewModel> VM getViewModel(Class<VM> modelClass);

    LifecycleOwner getLifecycleOwner();
}
