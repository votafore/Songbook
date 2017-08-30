package com.votafore.songbook.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.votafore.songbook.FireApp;
import com.votafore.songbook.R;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;


public class FragmentSettings extends Fragment {

    public static FragmentSettings newInstance(){
        return new FragmentSettings();
    }

    public FragmentSettings(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_song, container, false);
        return v;
    }
}
