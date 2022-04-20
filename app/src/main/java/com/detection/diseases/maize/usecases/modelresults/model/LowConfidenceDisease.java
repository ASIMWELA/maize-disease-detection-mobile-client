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
 * A Model for a disease with second largest probability of the detected disease
 *
 * Returns the disease with symptoms and precsriptions
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LowConfidenceDisease {


    /**
     * The disease unique identifier
     */
    String diseaseUuid;

    /**
     * The disease name
     */
    String diseaseName;

    /**
     * The stringified accuracy percentage returned by the model
     */
    String accuracy;

    /**
     * The symptoms of this disease
     */
    List<String> symptoms;

    /**
     * Measures on how to deal with the disease
     */
    List<String> prescriptions;

    /**
     * Turns an object to a string
     *
     * @return Stringified object
     */
    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, LowConfidenceDisease.class);
    }
}
