package com.bzh.dytt;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        doResume();
    }

    @Override
    protected void onPause() {
        doPause();
        MobclickAgent.onPause(this);
        super.onPause();
    }

    protected void doCreate(){}

    protected void doResume(){}

    protected void doPause(){}
}
