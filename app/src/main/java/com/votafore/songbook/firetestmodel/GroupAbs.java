package com.votafore.songbook.firetestmodel;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by User on 23.05.2017.
 *
 * Class represents a group of songs
 */

public abstract class GroupAbs {

    public String id;
    public String title;

    public GroupAbs(){}

    public abstract void setNode(DatabaseReference node);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
