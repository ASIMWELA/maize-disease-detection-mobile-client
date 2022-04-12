package com.detection.diseases.maize.ui.community;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.VolleyController;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author Augustine
 *
 * A Community Presenter responsible for updating the view if there is a change in {@link com.detection.diseases.maize.ui.community.payload.Issue}
 *
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommunityPresenter  implements CommunityContract.Presenter {
    /**
     * The view that has to be updated
     */
    CommunityContract.View view;

    /**
     * The context of the view
     */
    Context context;

    /**
     * A Constructor responsible for creating objects of this Presenter
     *
     * @param view The base view where this presenter is responsible for updating
     *
     * @param context The context of the view
     */
    public CommunityPresenter(CommunityContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    /**
     * Creates a request for getting community issues
     *
     * Updates the View accordingly based on the response
     *
     *
     * @param page The starting page
     *
     * @param totalPages Total number of pages to get
     */
    @Override
    public void getCommunityIssues(int page, int totalPages) {
        if(page > totalPages || (totalPages-page == 0)){
            view.hideLoading();
        }else {
            view.showLoading();
            JsonObjectRequest serviceCategoriesRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstants.BASE_API_URL + "/community/issues?page="+page+"&size=10",
                null,
                response -> {
                    view.hideLoading();
                    view.onResponse(response);
                }, error -> {
            view.hideLoading();
            view.onError(error);
        });
        serviceCategoriesRequest.setTag(AppConstants.GET_COMMUNITY_ISSUES);
        VolleyController.getInstance(context).addToRequestQueue(serviceCategoriesRequest);
        }
    }
}