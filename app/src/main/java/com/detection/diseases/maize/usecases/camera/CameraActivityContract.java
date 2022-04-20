package com.detection.diseases.maize.usecases.camera;

import androidx.camera.lifecycle.ProcessCameraProvider;

import java.io.File;

/**
 * @author Augustine
 *
 * An interface that defines all the contracts of the camera Actitivy View and Presenter
 */
public interface CameraActivityContract {

    /**
     * View Contracts
     *
     * @see CameraActivity for method description
     */
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

    /**
     * Presenter contracts
     *
     * @see CameraActivityPresenter for methods description
     *
     *
     */
    interface Presenter{
        void loadGalleyBtnClicked();
        void capturePictureBtnClicked();
        void showHintClicked();
        void uploadFile(File file);

    }
}
