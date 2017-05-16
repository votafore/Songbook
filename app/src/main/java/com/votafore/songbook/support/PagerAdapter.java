package com.votafore.songbook.support;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> pages;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

        pages = new ArrayList<>();
    }

    public void addPage(Fragment page){

        if(pages.contains(page))
            return;

        pages.add(page);
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
