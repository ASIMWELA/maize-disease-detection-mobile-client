package com.detection.diseases.maize.ui.camera;

import android.content.Context;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import com.detection.diseases.maize.commons.AppConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;


import cz.msebera.android.httpclient.Header;

public class CameraActivityPresenter implements CameraActivityContract.Presenter {

    private final CameraActivityContract.View view;
    private final Context context;

    public CameraActivityPresenter(CameraActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }


    @Override
    public void loadGalleyBtnClicked() {
        view.loadGalley();
    }

    @Override
    public void capturePictureBtnClicked() {
        view.capturePicture();
    }

    @Override
    public void showHintClicked() {
        view.showHint();
    }

    @Override
    public void uploadFile(File file) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            params.put("image", file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        client.setTimeout(70000);
        client.setResponseTimeout(70000);
       // client.addHeader("Content-Type", "multipart/form-data");
        client.post(AppConstants.BASE_API_URL+"/model/detect", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                view.showProgressBar();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                view.hideProgressBar();
                view.onUploadSucess(new String(responseBody, StandardCharsets.UTF_8));
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                view.hideProgressBar();
                view.onUploadError(new String(responseBody, StandardCharsets.UTF_8));
            }
        });
    }


}
