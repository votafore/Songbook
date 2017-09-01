package com.votafore.songbook;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.votafore.songbook.fragments.FragmentCatalog;
import com.votafore.songbook.fragments.FragmentList;
import com.votafore.songbook.fragments.FragmentSettings;


public class ActivityMain extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;

    NavigationView navigationView;

    boolean shouldGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.menu_songs);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                shouldGoHome = false;

                mDrawerLayout.closeDrawers();

                switch (item.getItemId()){
                    case R.id.menu_songs:
                        setPage(FragmentList.newInstance());

                        return true;
                    case R.id.menu_download:
                        setPage(FragmentCatalog.newInstance());
                        shouldGoHome = true;
                        return true;
                    case R.id.menu_settings:
                        setPage(FragmentSettings.newInstance());
                        shouldGoHome = true;
                        return true;
                    default:
                        return false;
                }
            }
        });

        setPage(FragmentList.newInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        if(shouldGoHome){
            FragmentList f = FragmentList.newInstance();
            setPage(f);
            navigationView.setCheckedItem(R.id.menu_songs);
            shouldGoHome = false;
            setTitleByFragment(f);
            return;
        }

        super.onBackPressed();

        setTitleByFragment(null);
    }

    private void setPage(Fragment f){

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container_main);

        if(currentFragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_main, f).commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_main, f).commit();
        }

        setTitleByFragment(f);
    }

    public void setTitleByFragment(@Nullable Fragment f){

        Transition t = new TransitionSet();
        t.setInterpolator(new FastOutSlowInInterpolator());
        t.setDuration(300);
        t.setStartDelay(200);



        if(f == null)
            f = getSupportFragmentManager().findFragmentById(R.id.container_main);

        if(f instanceof FragmentSettings)
            setTitle(getString(R.string.menu_item_settings));

        if(f instanceof FragmentList)
            setTitle(getString(R.string.menu_item_songs));

        if(f instanceof FragmentCatalog)
            setTitle(getString(R.string.menu_item_catalog));

        getSupportActionBar().setSubtitle("");
    }
}
