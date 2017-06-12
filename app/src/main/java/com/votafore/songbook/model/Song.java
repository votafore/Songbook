package com.votafore.songbook.model;






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
