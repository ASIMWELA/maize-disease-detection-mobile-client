package com.detection.diseases.maize.ui.community.model;

import java.io.File;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GalleryImageModel {
    File image;

    public GalleryImageModel(File image){
        this.image = image;
    }

}
