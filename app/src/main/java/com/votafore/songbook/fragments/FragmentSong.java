package com.votafore.songbook.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
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


public class FragmentSong extends Fragment {

    String mSongID;

    String mTitle   = "";
    String mText    = "";

    public static FragmentSong getInstance(String songID){

        FragmentSong song = new FragmentSong();

        Bundle args = new Bundle();

        args.putString("ID", songID);

        song.setArguments(args);

        return song;
    }

    public FragmentSong(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        mSongID = args.getString("ID");

        Fetcher query = new Fetcher();
        query.tableName     = Base.TABLE_SONGS;
        query.filter        = "id=?";
        query.filterArgs    = new String[]{mSongID};

        Cursor song = FireApp.getInstance().getData(query);

        if(!song.moveToFirst())
            return;

        mTitle = song.getString(song.getColumnIndex("title"));
        mText  = song.getString(song.getColumnIndex("content"));

        mText = mText.replace("\\n", System.lineSeparator());
        mText = mText.replace("\nКуплет", "\n<b><u>Куплет</u></b>");
        mText = mText.replace("Куплет\n", "<b><u>Куплет</u></b>\n");
        mText = mText.replace("\nПрипев", "\n<b><u>Припев</u></b>");
        mText = mText.replace("\nМост", "\n<b><u>Мост</u></b>");
        mText = mText.replace("\nБридж", "\n<b><u>Бридж</u></b>");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.test_fragment_song, container, false);

        TextView title = v.findViewById(R.id.song_title);
        TextView text = v.findViewById(R.id.song_text);

        title.setText(mTitle);
        text.setText(Html.fromHtml(mText));

        return v;
    }
}
