package com.detection.diseases.maize.usecases.modelresults.model;

/**
 * @author Augustine
 *
 * A model class representing a single image from a drawables. Uses drawable ids
 */
public class ImageIdsHolder {

    /**
     * Image id from drawable
     */
    private final int image;

    /**
     * Constuctor to create an object of this class
     * @param image An id from a drawable source
     */
    public ImageIdsHolder(int image) {
        this.image = image;
    }
    public int getImage() {
        return image;
    }
}