package com.votafore.songbook.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.votafore.songbook.R;


public class FragmentSong extends Fragment {

    String mTitle;
    String mText;

    public static FragmentSong getInstance(String title, String text){

        FragmentSong song = new FragmentSong();

        Bundle args = new Bundle();

        args.putString("title", title);
        args.putString("text", text);

        song.setArguments(args);

        return song;
    }

    public FragmentSong(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        mTitle = args.getString("title");
        mText  = args.getString("text");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.test_fragment_song, container, false);

        TextView title = (TextView) v.findViewById(R.id.song_title);
        TextView text = (TextView) v.findViewById(R.id.song_text);

        title.setText(mTitle);
        text.setText(mText);

        // TODO: заполнить содержимое

        return v;
    }
}
