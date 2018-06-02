package com.bzh.dytt

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.bzh.dytt.ui.AllMoviePageFragment
import com.bzh.dytt.ui.ImdbMovieFragment
import com.bzh.dytt.ui.NewMovieFragment
import com.google.android.gms.ads.MobileAds
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var pagerAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        toolbar.setTitle(R.string.nav_home_page)

        MobileAds.initialize(this, ADMOB_APP_ID)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // You should keep this limit low, especially if your pages have complex layouts.
        content_container.offscreenPageLimit = 1
        pagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        content_container.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_search) {
            SingleActivity.startSearchPage(this)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.nav_home -> {
                toolbar.setTitle(R.string.nav_home_page)
                content_container.currentItem = 0
            }
            R.id.nav_movie -> {
                toolbar.setTitle(R.string.nav_movie_page)
                content_container.currentItem = 1
            }
            R.id.nav_imdb -> {
                toolbar.setTitle(R.string.nav_imdb_page)
                content_container.currentItem = 2
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun supportFragmentInjector() = fragmentInjector

    class MainViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): BaseFragment = when (position) {
            0 -> NewMovieFragment.newInstance()
            1 -> AllMoviePageFragment.newInstance()
            2 -> ImdbMovieFragment.newInstance()
            else -> throw IndexOutOfBoundsException()
        }

        override fun getCount() = 3
    }

    companion object {
        val ADMOB_APP_ID = "ca-app-pub-8112052667906046~4830848371"
    }
}