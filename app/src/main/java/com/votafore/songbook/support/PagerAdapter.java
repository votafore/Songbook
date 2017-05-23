package com.votafore.songbook.support;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.votafore.songbook.FIreApp;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Class give possibilities to list groups of songs
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> pages;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

        pages = new ArrayList<>();

        Fetcher params = new Fetcher();
        params.tableName = Base.TABLE_GROUPS;

        Cursor data = FIreApp.getInstance().getData(params);

        if(!data.moveToFirst())
            return;

        do{
            FragmentPage page = FragmentPage.getInstance(
                    data.getString(data.getColumnIndex("title")),
                    data.getInt(data.getColumnIndex("id"))
            );

            pages.add(page);

        }while (data.moveToNext());
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
