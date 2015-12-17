package com.akili.etc.triviacrashsaga.Entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by kangleif on 11/18/2015.
 */
public class BadgeItem implements Serializable{
    private Bitmap image;
    private String title;
    private String description;

    public BadgeItem(Bitmap image, String title, String description) {
        super();
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
