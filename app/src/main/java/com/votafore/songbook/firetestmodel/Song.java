package com.votafore.songbook.firetestmodel;

/**
 * Created by User on 23.05.2017.
 *
 * The class represents a song record in Fire database
 */

public class Song {

    public String id;
    public String title;
    public String text;

    public Song(){}

    public Song(String id, String title, String content) {
        this.id     = id;
        this.title  = title;
        this.text   = content;
    }
}
