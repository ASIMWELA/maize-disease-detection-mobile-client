package com.detection.diseases.maize.ui.crops;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONObject;

public interface CropFragmentContract {
    interface View{
        void onOpenCamera();
        void onLocationAccess(FusedLocationProviderClient locationProviderClient);
        void onLocationAccessDenied();
        void onGetDataSucess(JSONObject response);
        void onDataError(VolleyError error);
        void showLoading();
        void hideLoading();

    }
    interface Presenter{
        void openCameraBtnClicked();
        void getLocation(double latitude, double longitude);
    }
}
