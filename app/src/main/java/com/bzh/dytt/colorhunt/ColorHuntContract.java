package com.bzh.dytt.colorhunt;

import android.arch.lifecycle.ViewModel;

import com.bzh.dytt.IPresenter;
import com.bzh.dytt.IView;

public interface ColorHuntContract {

    interface View extends IView<Presenter> {


    }

    interface Presenter extends IPresenter {

    }
}
