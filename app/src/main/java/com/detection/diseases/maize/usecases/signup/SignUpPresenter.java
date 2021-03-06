package com.detection.diseases.maize.usecases.signup;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.VolleyController;

import org.json.JSONObject;

public class SignUpPresenter implements SignupContract.Presenter {
    private final Context context;
    private final SignupContract.View view;

    public SignUpPresenter(Context context, SignupContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void sendSendSignupRequest(JSONObject data) {
        if(view.validateInput()){
            view.showLoadingProgress();

            JsonObjectRequest signupRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstants.BASE_API_URL+"/users",
                    data,
                    response -> {
                        view.hideLoadingProgress();
                        view.onSignupSucess(response);
                    }, error -> {
                view.onSignupError(error);
                view.hideLoadingProgress();
            });
            VolleyController.getInstance(context).addToRequestQueue(signupRequest);
        }else {
            view.onValidationError();
        }

    }

    @Override
    public void initiateInputValidation() {
        view.validateInput();
    }
}
