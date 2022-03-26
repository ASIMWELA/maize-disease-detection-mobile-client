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

public class AnswerIssuePresenter implements AnswerIssueContract.Presenter {

    private final AnswerIssueContract.View view;
    private final Context context;

    public AnswerIssuePresenter(AnswerIssueContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

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

    @Override
    public void initValidation() {
        view.validateInput();
    }

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
