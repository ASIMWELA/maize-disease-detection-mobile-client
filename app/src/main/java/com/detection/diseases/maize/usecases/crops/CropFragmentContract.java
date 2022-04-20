package com.detection.diseases.maize.usecases.crops;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONObject;

/**
 * @author Augustine
 *
 * Gives the contract for crops fragment
 */
public interface CropFragmentContract {
    /**
     * For all description on the individual methods see {@link CropsFragment}
     */
    interface View{
        void onOpenCamera();
        void onLocationAccess(FusedLocationProviderClient locationProviderClient);
        void onLocationAccessDenied();
        void onGetDataSucess(JSONObject response);
        void onDataError(VolleyError error);
        void showLoading();
        void hideLoading();

    }

    /**
     * For description on individual methods see {@link CropsPresenter}
     */
    interface Presenter{
        void openCameraBtnClicked();
        void getLocation(double latitude, double longitude);
    }
}
