package com.example.hospital.firstmenu.listView;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Admin on 2018-04-13.
 */

public class RecyclerItem {
    private int rank;
    private Uri image;
    private String name;
    private int score;

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public Uri getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public RecyclerItem(int rank, Uri image, String name, int score) {
        this.rank = rank;
        this.image = image;
        this.name = name;
        this.score = score;
    }

}
