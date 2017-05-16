package com.votafore.songbook;

import android.app.Application;
import android.util.Log;

import com.votafore.songbook.support.ListItem;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private List<ListItem> mChoosenSongs;

    private static App mThis;

    @Override
    public void onCreate() {
        super.onCreate();

        mThis = this;

        mChoosenSongs = new ArrayList<>();
    }

    public static App getInstance(){
        return mThis;
    }

    public void addSong(ListItem item){

        if(mChoosenSongs.contains(item))
            return;

        Log.v("message", "Добавили пестню");

        mChoosenSongs.add(item);
    }
}
