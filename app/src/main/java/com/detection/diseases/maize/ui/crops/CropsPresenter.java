package com.detection.diseases.maize.ui.crops;

public class CropsPresenter implements CropFragmentContract.Presenter{
    private final CropFragmentContract.View view;

    public CropsPresenter(CropFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void openCameraBtnClicked() {
        view.onOpenCamera();
    }
}