package com.bzh.dytt

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bzh.dytt.di.Injectable

abstract class BaseFragment : Fragment(), Injectable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return doCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        doResume()
    }

    override fun onPause() {
        doPause()
        super.onPause()
    }

    override fun onDestroyView() {
        doDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        doDestroy()
        super.onDestroy()
    }

    protected open fun doCreate(savedInstanceState: Bundle?) {}

    protected abstract fun doCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    protected open fun doViewCreated(view: View, savedInstanceState: Bundle?) {}

    protected open fun doDestroyView() {}

    protected open fun doDestroy() {}

    protected open fun doResume() {
        // Override this method in derived fragment
        // Do not do anything here
    }

    protected open fun doPause() {
        // Override this method in derived fragment
        // Do not do anything here
    }
}
