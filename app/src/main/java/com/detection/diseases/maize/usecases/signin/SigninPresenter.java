package com.detection.diseases.maize.usecases.signin;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.VolleyController;

import org.json.JSONObject;

public class SigninPresenter implements SigninContract.Presenter {

    private final SigninContract.View view;
    private final Context context;

    public SigninPresenter(SigninContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void sendSignRequest(JSONObject data) {
        if (view.validateInput()) {
            view.showLoading();
            JsonObjectRequest singInRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstants.BASE_API_URL + "/auth/login",
                    data,
                    response -> {
                        view.hideLoading();
                        view.onSigninSucess(response);
                    },
                    error -> {
                        view.hideLoading();
                        view.onsigninError(error);
                    });
            VolleyController.getInstance(context).addToRequestQueue(singInRequest);
        } else {
            view.onValidationError();
        }

    }

    @Override
    public void initInputValidation() {
        view.validateInput();
    }
}