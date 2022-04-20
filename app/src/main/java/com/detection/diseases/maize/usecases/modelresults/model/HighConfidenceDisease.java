package com.detection.diseases.maize.usecases.modelresults.model;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Augustine
 *
 * A Model for a disease with large probability of the detected disease
 *
 * Returns the disease with symptoms and precsriptions
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HighConfidenceDisease {

    String diseaseUuid,
            diseaseName,
            accuracy;
    List<String> symptoms;
    List<String> prescriptions;

    /**
     * Turns an object to a string
     *
     * @return Stringified object
     */
    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, HighConfidenceDisease.class);
    }
}
