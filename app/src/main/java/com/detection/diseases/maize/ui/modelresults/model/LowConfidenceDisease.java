package com.detection.diseases.maize.ui.modelresults.model;

import androidx.annotation.NonNull;

import com.detection.diseases.maize.ui.signin.LoggedInUserModel;
import com.google.gson.GsonBuilder;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LowConfidenceDisease {
    String diseaseUuid;
    String diseaseName;
    String accuracy;
    List<String> symptoms;
    List<String> prescriptions;
    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, LowConfidenceDisease.class);
    }
}
