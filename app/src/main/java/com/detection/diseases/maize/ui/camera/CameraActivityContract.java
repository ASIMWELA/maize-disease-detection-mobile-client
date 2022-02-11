package com.detection.diseases.maize.ui.camera;

import androidx.camera.lifecycle.ProcessCameraProvider;

import java.io.File;

public interface CameraActivityContract {
    interface View{
        void capturePicture();
        void showHint();
        void loadGalley();
        void startCamera(ProcessCameraProvider cameraProvider);
        void showProgressBar();
        void hideProgressBar();
        void onUploadSucess(String response);
        void onUploadError(String errorResponse);
    }
    interface Presenter{
        void loadGalleyBtnClicked();
        void capturePictureBtnClicked();
        void showHintClicked();
        void uploadFile(File file);

    }
}
