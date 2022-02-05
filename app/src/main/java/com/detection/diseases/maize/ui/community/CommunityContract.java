package com.detection.diseases.maize.ui.community;

import androidx.lifecycle.LiveData;

import com.android.volley.VolleyError;

import java.util.List;

public interface CommunityContract {
    interface View{
        void onGetIssuesSuccess(LiveData<List<String>> issues);
        void onGetIssuesError(VolleyError volleyError);
        void onViewDestroyed();
        void onGetNextPageIssues();
    }
    interface Presenter{
        void getCommunityIssues();
    }
}
