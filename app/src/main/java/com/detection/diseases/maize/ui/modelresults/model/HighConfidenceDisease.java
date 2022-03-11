package com.detection.diseases.maize.ui.modelresults.model;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, HighConfidenceDisease.class);
    }
}
