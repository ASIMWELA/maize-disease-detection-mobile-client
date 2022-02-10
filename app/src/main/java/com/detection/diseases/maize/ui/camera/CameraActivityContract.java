package com.detection.diseases.maize.ui.camera;

import androidx.camera.lifecycle.ProcessCameraProvider;

public interface CameraActivityContract {
    interface View{
        void capturePicture();
        void showHint();
        void loadGalley();
        void startCamera(ProcessCameraProvider cameraProvider);
    }
    interface Presenter{
        void loadGalleyBtnClicked();
        void capturePictureBtnClicked();
        void showHintClicked();
    }
}
