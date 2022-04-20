package com.detection.diseases.maize.usecases.community;

import com.android.volley.VolleyError;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * @author Augustine
 *
 * A Contract for answering issues activity
 */
public interface AnswerIssueContract {

    /**
     * For description on all the methods look at {@link AnswerAnIssueActivity}
     *
     */
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

    /**
     * For description on all the methods look at {@link AnswerIssuePresenter}
     */
    interface Presenter{
        void sendAnswer(RequestParams data, String issueUuid, String userUuid, String token);
        void initValidation();
        void getIssueAnswers(String issueUuid);
    }
}
