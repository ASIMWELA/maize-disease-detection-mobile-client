package com.detection.diseases.maize.usecases.community.model;

import java.io.File;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @author Augustine
 *
 * Defines a crops model for defining a single view in the Galley bottom sheet
 *
 * Allows to have a custom structure of all the images
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GalleryImageModel {

    /**
     * Hols a single file
     */
    File image;

    public GalleryImageModel(File image){
        this.image = image;
    }

}
