package com.detection.diseases.maize.ui.crops;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CropsPresenter extends ViewModel {

    private MutableLiveData<String> mText;

    public CropsPresenter() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}