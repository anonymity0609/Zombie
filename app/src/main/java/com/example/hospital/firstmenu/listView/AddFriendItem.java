package com.example.hospital.firstmenu.listView;

import android.net.Uri;

/**
 * Created by Admin on 2018-05-22.
 */

public class AddFriendItem {
    String name;
    Uri image;
    String id;

    public AddFriendItem(String name, Uri image, String id) {
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Uri getImage() {
        return image;
    }

    public String getId() {
        return id;
    }
}
