package com.votafore.songbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.votafore.songbook.fragments.FragmentList;


public class ActivityMain extends AppCompatActivity {

    FragmentManager mFragmentManager;
    Fragment pageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageList = FragmentList.newInstance();

        mFragmentManager = getSupportFragmentManager();

        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.container_main);

        if(currentFragment == null)
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container_main, pageList).commit();
    }

    // TODO: есть идея, получить все песни из базы, сделать список
    // использовать его для адаптера
    // при выборе песни использовать уже готовый объект что бы не обращаться к базе данных
}
