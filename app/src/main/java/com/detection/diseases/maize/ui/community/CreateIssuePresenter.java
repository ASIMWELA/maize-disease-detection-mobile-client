package com.detection.diseases.maize.ui.community;

import android.content.Context;

import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.HttpAsyncHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;

public class CreateIssuePresenter implements CreateIssueContract.Presenter {
    private final Context context;
    private final CreateIssueContract.View view;

    public CreateIssuePresenter(Context context, CreateIssueContract.View view) {
        this.context = context;
        this.view = view;
    }


    @Override
    public void sendIssueRequest(RequestParams data,String userUuid, String token) {
        if (view.validateInputs()) {
            AsyncHttpClient asyncHttpClient = HttpAsyncHelper.getInstance(context).getHttpClient();
            asyncHttpClient.addHeader("Authorization", "Bearer " + token);
            asyncHttpClient.post(AppConstants.BASE_API_URL + "/community/post-issue/"+userUuid, data, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    view.showProgress();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    view.hideProgress();
                    view.onSendIssueSuccess(new String(responseBody, StandardCharsets.UTF_8));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    view.hideProgress();
                    if (responseBody != null) {
                        view.onSendIssueError(new String(responseBody, StandardCharsets.UTF_8));
                    } else {
                        view.onSendIssueError("Server failed to process your request");
                    }
                }
            });


        } else {
            view.onValidationFailure();
        }

    }

    @Override
    public void initValidation() {
        view.validateInputs();
    }
}
