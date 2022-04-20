package com.detection.diseases.maize.usecases.signin;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface SigninContract {
    interface View{
        boolean validateInput();
        void onSigninSucess(JSONObject response);
        void onsigninError(VolleyError error);
        void onValidationError();
        void showLoading();
        void hideLoading();
    }
    interface Presenter{
        void sendSignRequest(JSONObject data);
        void initInputValidation();
    }
}
