package com.votafore.songbook.firetestmodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 23.05.2017.
 *
 * The class represents a song record in Fire database
 */

public class Song {

    public int id;
    public String title;
    public String content;

    public Song(){}

    public Song(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
