package com.votafore.songbook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.votafore.songbook.fragments.FragmentList;


public class ActivityMain extends AppCompatActivity implements FireApp.onSongDownloadCompleteListener {

    FragmentManager mFragmentManager;
    Fragment pageList;

    ProgressDialog mDialog;

    public static final String TAG = "TAGUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "ActivityMain: onCreate");

        FireApp.getInstance().setOnDownloadCompleteListener(this);

//        if(app.setOnDownloadCompleteListener(this)){
//
//            mDialog = new ProgressDialog(this);
//            mDialog.setCancelable(true);
//            mDialog.setMessage("Обновление списка");
//
//            mDialog.show();
//        }else{
//            showList();
//        }

        showList();
    }

    @Override
    public void onUpdateStart() {

        Log.v(TAG, "ActivityMain: onUpdateStart");

        if(mDialog != null)
            return;

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Обновление списка");

        mDialog.show();
    }

    @Override
    public void onUpdateComplete() {

        Log.v(TAG, "ActivityMain: onUpdateComplete");

        mDialog.dismiss();
        mDialog = null;

        showList();
    }

    private void showList(){

        Log.v(TAG, "ActivityMain: showList");

        mFragmentManager = getSupportFragmentManager();

        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.container_main);

        pageList = FragmentList.newInstance();

        if(currentFragment != null){
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.container_main, pageList).commit();
        }else{
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container_main, pageList).commit();
        }
    }
}
