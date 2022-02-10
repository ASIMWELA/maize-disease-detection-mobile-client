package com.detection.diseases.maize.ui.camera;

import android.content.Context;

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


}
