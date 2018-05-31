package com.bzh.dytt

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import com.umeng.analytics.MobclickAgent

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        MobclickAgent.onPause(this)
        super.onPause()
    }
}