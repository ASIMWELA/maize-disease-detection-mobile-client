package com.detection.diseases.maize.usecases.modelresults.model;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Augustine
 *
 * A model class that is returned from a remote API containing first and second disease
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ModelResponse {
    HighConfidenceDisease firstDisease;
    LowConfidenceDisease secondDisease;
    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, ModelResponse.class);
    }
}
