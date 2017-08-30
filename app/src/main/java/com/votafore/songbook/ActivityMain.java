package com.votafore.songbook;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.votafore.songbook.fragments.FragmentCatalog;
import com.votafore.songbook.fragments.FragmentList;
import com.votafore.songbook.fragments.FragmentSettings;


public class ActivityMain extends AppCompatActivity {

    FragmentManager mFragmentManager;

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;

    NavigationView navigationView;

    boolean shouldGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_open, R.string.app_name);

        mDrawerLayout.setDrawerListener(mToggle);

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
            setPage(FragmentList.newInstance());
            navigationView.setCheckedItem(R.id.menu_songs);
            shouldGoHome = false;
            return;
        }

        super.onBackPressed();
    }

    private void setPage(Fragment f){

        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.container_main);

        if(currentFragment != null){
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.container_main, f).commit();
        }else{
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container_main, f).commit();
        }
    }
}
