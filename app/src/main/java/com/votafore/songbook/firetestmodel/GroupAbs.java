package com.votafore.songbook.firetestmodel;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by User on 23.05.2017.
 *
 * Class represents a group of songs
 */

public abstract class GroupAbs {

    public int id;
    public String title;
    public String key;

    public GroupAbs(){}

    public abstract void setNode(DatabaseReference node);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
