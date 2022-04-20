package com.detection.diseases.maize.usecases.camera;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.HttpAsyncHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;


import cz.msebera.android.httpclient.Header;

/**
 * @author Augustine
 *
 * Camera activity presenter
 *
 * Updates the view when actions that request the view to be changed are fired
 *
 *
 */
public class CameraActivityPresenter implements CameraActivityContract.Presenter {

    /**
     *A View object that determines which and how to update the view
     */
    private final CameraActivityContract.View view;

    /**
     * Context where the presenter is initialised
     *
     * @see Context
     */
    private final Context context;


    /**
     * Constructor to create an object of this class
     *
     * @param view The view where the object will be created
     *
     * @param context The context where the object will be created
     */
    public CameraActivityPresenter(CameraActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }


    /**
     * Loads galley when invoked
     *
     * Invoked in a button click listener
     */
    @Override
    public void loadGalleyBtnClicked() {
        view.loadGalley();
    }

    /**
     * Captures an image when invoked
     *
     * Invoked in a button click listener
     */
    @Override
    public void capturePictureBtnClicked() {
        view.capturePicture();
    }

    /**
     * Pops up a dialogView when invoked. Shows the steps to follow when using the activity camera
     */
    @Override
    public void showHintClicked() {
        view.showHint();
    }

    /**
     * A post request for a diseased leaf.
     *
     * @param file An image file that has to be uploaded
     */
    @Override
    public void uploadFile(File file) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = HttpAsyncHelper.getInstance(context).getHttpClient();
        try {
            params.put("image", file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
       // client.addHeader("Content-Type", "multipart/form-data");
        client.post(AppConstants.BASE_API_URL+"/model/detect", params, new AsyncHttpResponseHandler() {
            @Override
            public void setTag(Object TAG) {
                super.setTag(AppConstants.POST_IMAGE_TAG_REQUEST);

            }

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
                if(responseBody !=null){
                    view.onUploadError(new String(responseBody, StandardCharsets.UTF_8));
                }else {
                    view.onUploadError("Server failed to process your request");
                }

            }


        });

    }


}
