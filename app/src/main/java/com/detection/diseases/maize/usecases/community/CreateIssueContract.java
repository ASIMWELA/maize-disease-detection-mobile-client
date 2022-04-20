package com.detection.diseases.maize.usecases.community;

import com.loopj.android.http.RequestParams;

/**
 * @author Augustine
 *
 * Creates a contract for the Create Issue View and a Presenter responsible for updating the view
 */
public interface CreateIssueContract {
    /**
     * For description of respective methods look at _{@link CreateAnIssueActivity}
     */
    interface View{
        boolean validateInputs();
        void onValidationFailure();
        void onSendIssueSuccess(String response);
        void onSendIssueError(String errorResponse);
        void showProgress();
        void hideProgress();
    }

    /**
     * For description of respective methods look at _{@link CreateIssuePresenter}
     */
    interface Presenter{
        void sendIssueRequest(RequestParams data, String userUuid,String token);
        void initValidation();
    }
}
