package com.votafore.songbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.support.FragmentSong;

public class ActivitySong extends AppCompatActivity {

    private String mSongID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);


        Intent args = getIntent();

        mSongID = args.getStringExtra("ID");

        Fetcher query = new Fetcher();
        query.tableName     = Base.TABLE_SONGS;
        query.filter        = "id=?";
        query.filterArgs    = new String[]{mSongID};

        Cursor song = FIreApp.getInstance().getData(query);

        if(!song.moveToFirst())
            return;

//        TextView title = (TextView) findViewById(R.id.song_activity_title);
//        TextView content = (TextView) findViewById(R.id.song_activity_content);
//
//
//        title.setText(song.getString(song.getColumnIndex("title")));
//
//        String text = song.getString(song.getColumnIndex("content"));
//
//        // TODO: реализовать форматирование в одном месте
//
//        text = text.replace("\nКуплет", "\n<b><u>Куплет</u></b>");
//        text = text.replace("Куплет\n", "<b><u>Куплет</u></b>\n");
//        text = text.replace("\nПрипев", "\n<b><u>Припев</u></b>");
//        text = text.replace("\nМост", "\n<b><u>Мост</u></b>");
//        text = text.replace("\nБридж", "\n<b><u>Бридж</u></b>");
//
//        text = text.replace("\n", "<br>");
//
//        content.setText(Html.fromHtml(text));

        FragmentSong f_song = FragmentSong.getInstance(
                song.getString(song.getColumnIndex("title")),
                song.getString(song.getColumnIndex("content")));

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, f_song, "tag").commit();
    }
}
