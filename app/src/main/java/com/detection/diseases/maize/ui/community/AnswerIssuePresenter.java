package com.detection.diseases.maize.ui.community;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.HttpAsyncHelper;
import com.detection.diseases.maize.helpers.VolleyController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import lombok.SneakyThrows;

/**
 * @author Augustine
 *
 * Answer Issue Presenter
 *
 * Updates the view based on the change in {@link com.detection.diseases.maize.ui.community.model.IssueAnswerModel}
 */
public class AnswerIssuePresenter implements AnswerIssueContract.Presenter {

    /**
     * The view to be updated, implemented in {@link AnswerAnIssueActivity}
     */
    private final AnswerIssueContract.View view;

    /**
     * The context of the view to be updated
     */
    private final Context context;

    /**
     * Constructor for creating an object of this class
     *
     * @param view The base view to be updated. See {@link AnswerIssueContract.View}
     *
     * @param context Context of the view. See {@link Context}
     */
    public AnswerIssuePresenter(AnswerIssueContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    /**
     * Creates a post request for creating an answer to an issue and sending it to a remote API
     *
     * @param data Data for issue answer
     *
     * @param issueUuid A unique identifier of the issue
     *
     * @param userUuid A unique identifier of the user who is sending the issue
     *
     * @param token An Authorization token for creating answer to an issue. Created when a user is logged in and his/her session is activite
     */
    @Override
    public void sendAnswer(RequestParams data, String issueUuid, String userUuid, String token) {
        if (view.validateInput()) {
            AsyncHttpClient asyncHttpClient = HttpAsyncHelper.getInstance(context).getHttpClient();
            asyncHttpClient.addHeader("Authorization", "Bearer " + token);
            asyncHttpClient.post(
                    AppConstants.BASE_API_URL + "/community/issues/answer/" + issueUuid + "/" + userUuid,
                    data,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void setTag(Object TAG) {
                            super.setTag(AppConstants.SEND_ISSUE_ANSWER);
                        }

                        @Override
                        public void onStart() {
                            view.showLoading();
                        }

                        @SneakyThrows
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            view.hideLoading();
                            if (statusCode == 200) {
                                String response = new String(responseBody, StandardCharsets.UTF_8);
                                JSONObject responseObject = new JSONObject(response);
                                view.onAnswerSuccess(responseObject);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            view.hideLoading();
                            if (responseBody != null) {
                                view.onAnswerError(new String(responseBody, StandardCharsets.UTF_8));
                            } else {
                                view.onAnswerError("Server failed to process your request");
                            }
                        }
                    });

        } else {
            view.onInputValidationFailed();
        }
    }

    /**
     * Kickstart input validation
     */
    @Override
    public void initValidation() {
        view.validateInput();
    }

    /**
     * A request to get all answers of a particular issue. The answers come paginated
     *
     * @param issueUuid A unique identifier of this issue
     */
    @Override
    public void getIssueAnswers(String issueUuid) {
        view.showGetIssueAnswerLoading();
        JsonObjectRequest serviceCategoriesRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstants.BASE_API_URL + "/community/answers/"+issueUuid+"?page=0&size=20",
                null,
                response -> {
                    view.hideLoading();
                    view.onGetIssueAnswerResponse(response);
                    view.hideGetIssueAnswerLoader();
                }, error -> {
            view.hideGetIssueAnswerLoader();
            view.onGetIssueAnswersError(error);
        });
        serviceCategoriesRequest.setTag(AppConstants.GET_ISSUE_ANSWERS);
        VolleyController.getInstance(context).addToRequestQueue(serviceCategoriesRequest);
    }
}
