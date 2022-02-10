package com.detection.diseases.maize.ui.crops;

public interface CropFragmentContract {
    interface View{
        void onOpenCamera();
    }
    interface Presenter{
        void openCameraBtnClicked();
    }
}
