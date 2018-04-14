package com.bzh.dytt;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bzh.dytt.colorhunt.ColorHuntFragment;
import com.bzh.dytt.home.AllMoviePageFragment;
import com.bzh.dytt.home.NewMovieFragment;
import com.bzh.dytt.view.NonInteractiveViewPage;
import com.google.android.gms.ads.MobileAds;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector {

    private static final String TAG = "MainActivity";
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @BindView(R.id.content_container)
    NonInteractiveViewPage mContainer;

    private MainViewPagerAdapter mPagerAdapter;


    private final static String ADMOB_APP_ID = "ca-app-pub-8112052667906046~4830848371";

    @Override
    protected void doCreate() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MobileAds.initialize(this, ADMOB_APP_ID);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        // You should keep this limit low, especially if your pages have complex layouts.
        mContainer.setOffscreenPageLimit(1);
        mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mContainer.setAdapter(mPagerAdapter);

    }

    @Override
    protected void doResume() {
        mToolbar.setTitle(R.string.nav_home_page);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            SingleActivity.startSearchPage(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mToolbar.setTitle(R.string.nav_home_page);
            mContainer.setCurrentItem(0);
        } else if (id == R.id.nav_movie) {
            mToolbar.setTitle(R.string.nav_movie_page);
            mContainer.setCurrentItem(1);
        } else if (id == R.id.nav_imdb) {
            mToolbar.setTitle(R.string.nav_imdb_page);
            mContainer.setCurrentItem(2);
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    private static class MainViewPagerAdapter extends FragmentPagerAdapter {

        MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return NewMovieFragment.newInstance();
                case 1:
                    return AllMoviePageFragment.newInstance();
                case 2:
                    return ColorHuntFragment.newInstance();
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
