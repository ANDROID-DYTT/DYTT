package com.bzh.dytt.colorhunt;

import android.text.TextUtils;
import android.util.Log;

import com.bzh.dytt.services.ColorHuntService;
import com.bzh.dytt.services.Entity.ColorHunt;
import com.bzh.dytt.services.ServiceManager;
import com.google.gson.Gson;

//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ColorHuntPresenter implements ColorHuntContract.Presenter {

    private static final String TAG = "ColorHuntPresenter";

    private ColorHuntContract.View mView;

    ColorHuntPresenter(ColorHuntContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

//    private Disposable mCurrentSequence = null;

//    private Observer<String> mColorHuntList = new Observer<String>() {
//
//        private String mRequestResult;
//
//        @Override
//        public void onSubscribe(Disposable d) {
//            mCurrentSequence = d;
//        }
//
//        @Override
//        public void onNext(String s) {
//            if (TextUtils.isEmpty(s)) {
//                return;
//            }
//            int prefix = s.indexOf('[');
//            int suffix = s.lastIndexOf(']');
//            if (prefix != -1 && suffix != -1) {
//                mRequestResult = s.substring(prefix, suffix + 1);
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//
//        }
//
//        @Override
//        public void onComplete() {
//            if (mRequestResult == null) {
//                return;
//            }
//
//            Gson gson = new Gson();
//            ColorHunt[] colorHunts = gson.fromJson(mRequestResult, ColorHunt[].class);
//            for (ColorHunt colorHunt : colorHunts) {
//                if (colorHunt == null) {
//                    continue;
//                }
//                try {
//                    colorHunt.setColor1(colorHunt.getCode().substring(0, 6));
//                    colorHunt.setColor2(colorHunt.getCode().substring(6, 12));
//                    colorHunt.setColor3(colorHunt.getCode().substring(12, 18));
//                    colorHunt.setColor4(colorHunt.getCode().substring(18, 24));
//                } catch (Exception e) {
//                    Log.e(TAG, "onComplete: ", e);
//                }
//            }
//        }
//    };
//
//    @Override
//    public void subscribe() {
//
//        ColorHuntService.ColorHuntBody body = new ColorHuntService.ColorHuntBody(0, ColorHuntService.ColorHuntSort.Random, "");
//        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), body.toString());
//
//        ServiceManager
//                .getInstance()
//                .getColorHuntService()
//                .listColor(requestBody)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(mColorHuntList);
//    }
//
//    @Override
//    public void unSubscribe() {
//        if (mCurrentSequence != null && !mCurrentSequence.isDisposed()) {
//            mCurrentSequence.dispose();
//        }
//    }
}
