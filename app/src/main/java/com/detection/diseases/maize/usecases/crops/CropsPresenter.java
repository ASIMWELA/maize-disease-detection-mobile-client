package com.detection.diseases.maize.usecases.crops;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.VolleyController;

/**
 * @author Augustine
 *
 * A Crops presenter responsible for updating crop
 */
public class CropsPresenter implements CropFragmentContract.Presenter{
    /**
     * A View to be updated
     */
    private final CropFragmentContract.View view;

    /**
     * The context where the view will be updated
     */
    private final Context context;

    /**
     * A Constructor
     * @param view The view to be updated
     * @param context The context where the view will be updated
     */
    public CropsPresenter(CropFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    /**
     * Triggered when an open camera button is clicked
     */
    @Override
    public void openCameraBtnClicked() {
        view.onOpenCamera();
    }

    /**
     * Request a user location using his/her current location
     *
     * @param latitude  User's latitude
     *
     * @param longitude User's longitude
     */
    @Override
    public void getLocation(double latitude, double longitude) {
        view.showLoading();
        JsonObjectRequest weatherData = new JsonObjectRequest(
                Request.Method.GET,
                AppConstants.OPENWEATHERAPI +"?lat="+latitude+"&lon="+longitude+"&units=metric&"+"appid="+AppConstants.OPENWEATHERAPIKEY,
                null,
                response -> {
                    view.hideLoading();
                    view.onGetDataSucess(response);
                }, error -> {
            view.hideLoading();
            view.onDataError(error);
        });

        VolleyController.getInstance(context).addToRequestQueue(weatherData);
    }

}