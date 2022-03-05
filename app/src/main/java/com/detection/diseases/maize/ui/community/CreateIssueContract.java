package com.detection.diseases.maize.ui.community;

import com.loopj.android.http.RequestParams;

public interface CreateIssueContract {
    interface View{
        boolean validateInputs();
        void onValidationFailure();
        void onSendIssueSuccess(String response);
        void onSendIssueError(String errorResponse);
        void showProgress();
        void hideProgress();
    }
    interface Presenter{
        void sendIssueRequest(RequestParams data, String userUuid,String token);
        void initValidation();
    }
}
