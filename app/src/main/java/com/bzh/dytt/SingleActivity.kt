package com.bzh.dytt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.bzh.dytt.search.SearchFragment
import com.bzh.dytt.ui.VideoDetailPageFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class SingleActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = fragmentInjector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_single)

        val type = intent.getSerializableExtra(TYPE)

        val fragment: BaseFragment = when (type) {
            SingleType.Detail -> {
                val detailLink = intent.getStringExtra(DATA)
                VideoDetailPageFragment.newInstance(detailLink)
            }
            else -> {
                SearchFragment.newInstance()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    fun setupActionBar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    enum class SingleType {
        Detail(),
        Search()
    }

    companion object {
        const val TYPE = "TYPE"
        const val DATA = "DATA"

        fun startDetailPage(activity: Activity, detailLink: String) {
            val intent = Intent(activity, SingleActivity::class.java)
            intent.putExtra(TYPE, SingleActivity.SingleType.Detail)
            intent.putExtra(DATA, detailLink)
            activity.startActivity(intent)
        }

        fun startSearchPage(activity: Activity) {
            val intent = Intent(activity, SingleActivity::class.java)
            intent.putExtra(TYPE, SingleActivity.SingleType.Search)
            activity.startActivity(intent)
        }
    }
}