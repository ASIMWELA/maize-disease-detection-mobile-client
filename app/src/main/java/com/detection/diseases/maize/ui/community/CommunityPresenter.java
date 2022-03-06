package com.detection.diseases.maize.ui.community;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.VolleyController;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommunityPresenter  implements CommunityContract.Presenter {
    CommunityContract.View view;
    Context context;

    public CommunityPresenter(CommunityContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getCommunityIssues(int page, int totalPages) {
        if(page > totalPages || (totalPages-page == 0)){
            Toast.makeText(context, "That's all the issues", Toast.LENGTH_SHORT).show();
            view.hideLoading();
        }else {
            view.showLoading();
            JsonObjectRequest serviceCategoriesRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstants.BASE_API_URL + "/community/issues?page="+page+"&size=3",
                null,
                response -> {
                    view.hideLoading();
                    view.onResponse(response);
                    view.stopReshresh();
                }, error -> {
            view.hideLoading();
            view.onError(error);
            view.stopReshresh();
        });
        serviceCategoriesRequest.setTag(AppConstants.GET_COMMUNITY_ISSUES);
        VolleyController.getInstance(context).addToRequestQueue(serviceCategoriesRequest);
        }
    }
}