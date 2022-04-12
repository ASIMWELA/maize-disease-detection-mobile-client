package com.detection.diseases.maize.ui.crops;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.VolleyController;

public class CropsPresenter implements CropFragmentContract.Presenter{
    private final CropFragmentContract.View view;
    private final Context context;

    public CropsPresenter(CropFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void openCameraBtnClicked() {
        view.onOpenCamera();
    }

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