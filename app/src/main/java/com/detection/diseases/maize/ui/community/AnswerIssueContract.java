package com.detection.diseases.maize.ui.community;

import com.android.volley.VolleyError;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public interface AnswerIssueContract {
    interface View{
        void onAnswerSuccess(JSONObject response);
        void onAnswerError(String error);
        void showLoading();
        void hideLoading();
        boolean validateInput();
        void onInputValidationFailed();
        void loadGalley();
        void showGetIssueAnswerLoading();
        void hideGetIssueAnswerLoader();
        void onGetIssueAnswersError(VolleyError error);
        void onGetIssueAnswerResponse(JSONObject response);
    }

    interface Presenter{
        void sendAnswer(RequestParams data, String issueUuid, String userUuid, String token);
        void initValidation();
        void getIssueAnswers(String issueUuid);
    }
}
