package com.example.hospital.firstmenu.listView;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Admin on 2018-05-20.
 */

public class FriendItem implements Comparable<FriendItem> {
    private Uri image;
    private String name;
    private int score;
    private String id;

    public Uri getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public FriendItem(Uri image, String name, int score, String id) {
        this.image = image;
        this.name = name;
        this.score = score;
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull FriendItem item) {
        return name.compareTo(item.getName());
    }
}
