package com.votafore.songbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.votafore.songbook.database.Fetcher;

public class ActivitySong extends AppCompatActivity {

    private int mSongID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        Intent args = getIntent();

        mSongID = args.getIntExtra("ID", -1);

        Fetcher query = new Fetcher();
        query.tableName = "Songs";
        query.filter = "id=?";
        query.filterArgs = new String[]{String.valueOf(mSongID)};

        Cursor song = App.getInstance().getData(query);

        if(!song.moveToFirst())
            return;

        TextView title = (TextView) findViewById(R.id.song_activity_title);
        TextView content = (TextView) findViewById(R.id.song_activity_content);


        title.setText(song.getString(song.getColumnIndex("title")));
        content.setText(song.getString(song.getColumnIndex("content")));
    }
}
