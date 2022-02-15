package com.detection.diseases.maize.ui.signup;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface SignupContract {
    interface View{
        boolean validateInput();
        void onValidationError();
        void showLoadingProgress();
        void hideLoadingProgress();
        void onSignupSucess(JSONObject response);
        void onSignupError(VolleyError error);

    }
    interface Presenter{
        void sendSendSignupRequest(JSONObject data);
        void initiateInputValidation();
    }
}
