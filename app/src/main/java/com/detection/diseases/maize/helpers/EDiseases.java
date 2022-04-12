package com.detection.diseases.maize.helpers;

/**
 * @author Augustine
 * <p>
 * An Enum for disease constansts
 */
public enum EDiseases {
    COMMON_RUST("Common rust"),
    NORTHERN_LEAF_BLIGHT("Northern leaf blight"),
    HEALTH("Health"),
    GRAY_LEAF_SPOT("Gray leaf spot");
    public final String label;
    EDiseases(String s) {
        this.label = s;
    }
}
