package com.detection.diseases.maize.usecases.community;

import android.content.Context;

import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.HttpAsyncHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;

/**
 * @author Augustine
 *
 * Responsible for updating {@link CreateAnIssueActivity view}
 */
public class CreateIssuePresenter implements CreateIssueContract.Presenter {
    /**
     * The context of the presenter
     */
    private final Context context;

    /**
     * The view to be updated
     */
    private final CreateIssueContract.View view;

    /**
     * The constructor for creating objects of this class
     *
     * @param context The presenter context
     *
     * @param view The view to be updated
     */
    public CreateIssuePresenter(Context context, CreateIssueContract.View view) {
        this.context = context;
        this.view = view;
    }


    /**
     * Create a request of sending issues to a Remote api
     *
     * Uses {@link HttpAsyncHelper} as an http client which is an implementation of {@link AsyncHttpClient}
     *
     * @param data Issue data, image and issue question, question dec and crop. uses {@link RequestParams}
     *
     * @param userUuid The unique identifier who creates the issue
     *
     * @param token An authorization token for the user to be allowed creating the issue
     */
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

    /**
     * Kick starts input validation upon lauching the activity
     */
    @Override
    public void initValidation() {
        view.validateInputs();
    }
}
