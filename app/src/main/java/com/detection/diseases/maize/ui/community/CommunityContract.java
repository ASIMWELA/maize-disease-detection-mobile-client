package com.detection.diseases.maize.ui.community;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface CommunityContract {
    interface View{
        void onError(VolleyError volleyError);
        void onViewDestroyed();
        void onResponse(JSONObject response);
        void showLoading();
        void hideLoading();
    }
    interface Presenter{
        void getCommunityIssues(int page, int totalPages);
    }
}
