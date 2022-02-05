package com.detection.diseases.maize.ui.community;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunityPresenter extends ViewModel implements CommunityContract.Presenter {
    private MutableLiveData<String> mText;

    public CommunityPresenter() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public void getCommunityIssues() {

    }
}