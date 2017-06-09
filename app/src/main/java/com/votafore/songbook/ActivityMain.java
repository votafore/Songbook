package com.votafore.songbook;

import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.votafore.songbook.fragments.FragmentList;
import com.votafore.songbook.support.FragmentSong;
import com.votafore.songbook.testrecview.AbstractExpandableDataProvider;
import com.votafore.songbook.testrecview.ExpandableAdapter;


public class ActivityMain extends AppCompatActivity {


    FragmentList pageList;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity_ex);

        pageList = FragmentList.newInstance();

        mFragmentManager = getSupportFragmentManager();

        Fragment currentFragment = mFragmentManager.findFragmentById(R.id.container_main);

        if(currentFragment == null)
            mFragmentManager.beginTransaction().add(R.id.container_main, pageList).commit();
    }
}
