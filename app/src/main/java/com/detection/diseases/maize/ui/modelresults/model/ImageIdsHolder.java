package com.detection.diseases.maize.ui.modelresults.model;

public class ImageIdsHolder {
    //set to String, if you want to add image url from internet
    private int image;

    public ImageIdsHolder(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}