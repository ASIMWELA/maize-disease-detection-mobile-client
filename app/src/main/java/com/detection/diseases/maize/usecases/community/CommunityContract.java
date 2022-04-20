package com.detection.diseases.maize.usecases.community;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * @author Augustine
 *
 * Defines all the contract for managing community interactions
 */
public interface CommunityContract {
    /**
     * For detailed description on the methods see {@link CommunityFragment}
     */
    interface View{
        void onError(VolleyError volleyError);
        void onResponse(JSONObject response);
        void showLoading();
        void hideLoading();
    }
    /**
     * For detailed description on the methods see {@link CommunityPresenter}
     */
    interface Presenter{
        void getCommunityIssues(int page, int totalPages);
    }
}
