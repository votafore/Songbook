package com.votafore.songbook;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class ActivityMain extends AppCompatActivity {

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity_ex);

//        pageList = FragmentList.newInstance();
//
//        mFragmentManager = getSupportFragmentManager();
//
//        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.container_main);
//
//        if(currentFragment == null)
//            mFragmentManager.beginTransaction()
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .add(R.id.container_main, pageList).commit();
    }
}
