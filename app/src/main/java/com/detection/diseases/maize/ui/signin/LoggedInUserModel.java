package com.detection.diseases.maize.ui.signin;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggedInUserModel {
    String uuid, email, firstName, lastName;
    boolean active;
    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, LoggedInUserModel.class);
    }
}
