package com.detection.diseases.maize.ui.community.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Augustine
 *
 *  A Model that defines the structure of the data to be put in a crops dialog selection
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CropsModel {
    /**
     * Defines a crop name
     */
    String cropName;

    /**
     * A drawable ID
     */
    int imageId;
}
