package com.detection.diseases.maize.usecases.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.TextValidator;
import com.detection.diseases.maize.helpers.VolleyController;
import com.detection.diseases.maize.usecases.community.payload.Issue;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;

/**
 * @author Augustine
 *
 * A Fragment responsible for all community section interaction on issues
 *
 * Responsible for displaying issues, liking issue, marking an issue as resolved, etc
 */
public class CommunityFragment extends Fragment implements CommunityContract.View {

    /**
     * A presenter object responsible for updating the view. See {@link CommunityPresenter}
     */
    private CommunityPresenter communityPresenter;

    /**
     * A recycler view for holding community issues
     */
    private RecyclerView recyclerView;

    /**
     * An editText responsible for searching community issues
     */
    private EditText etSearch;

    /**
     * A nested Scroll View responsible for pagination
     */
    private NestedScrollView nestedScrollView;

    /**
     * Showing get issues request progress
     */
    private ProgressBar progressBar;

    /**
     * A Chip view responsible for opening community section
     */
    private Chip askCommunity;

    /**
     * A Root view for this view. Responsible for attaching a snackBar for displaying Http request errors
     */
    private ConstraintLayout communityRootView;

    /**
     * An adapter that adapts A single community Issue Row into a view inflated into a recycler view.
     *
     * For more see {@link RecyclerView}
     */
    private IssuesRecyclerViewAdapter adapter;

    /**
     * A list holding community issues
     */
    List<Issue> issueList;

    /**
     * A TextView for displaying a message if there are no issues created in the app
     */
    private TextView noIssuesMessage;

    /**
     * Default page number and number of pages
     */
    private int page = 0, numberOfPages = 1;

    /**
     * A Chip to hold a custom back arrow
     */
    private Chip chBackChip;


    /**
     * Creates the view of this fragment
     *
     * @param inflater A layout inflater responsible for adding a layour view to this flagment
     *
     * @param container A Base view group for this Fragment
     *
     * @param savedInstanceState A bundle containing previous state of the fragment
     *
     * @return View
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_community, container, false);
        initComponent(root);

        communityPresenter.getCommunityIssues(page, numberOfPages);
        nestedScrollView.setNestedScrollingEnabled(true);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                // incrementing page number if reached bottom,
                ++page;
                communityPresenter.getCommunityIssues(page, numberOfPages);
            }
        });
        askCommunity.setOnClickListener(v -> {
            Intent createIssue = new Intent(requireContext(), CreateAnIssueActivity.class);
            startActivity(createIssue);
        });

        chBackChip.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        return root;
    }

    /**
     * Initialise all the views
     *
     * @param root A view that holds all the child views of the fragment
     */
    private void initComponent(View root) {
        recyclerView = root.findViewById(R.id.fg_community_issues_rv);
        etSearch = root.findViewById(R.id.fg_community_search_issue_tv);
        progressBar = root.findViewById(R.id.fg_community_pagination_pb);
        askCommunity = root.findViewById(R.id.fg_community_ask_cp);
        communityRootView = root.findViewById(R.id.fg_community_root_view);
        noIssuesMessage = root.findViewById(R.id.fg_community_tv_no_issues);
        nestedScrollView = root.findViewById(R.id.fg_community_nsc_view);
        issueList = new ArrayList<>();
        chBackChip = root.findViewById(R.id.community_chip_back);
        communityPresenter = new CommunityPresenter(this, requireContext());


    }

    /**
     * A Method invoked when there is an error when getting Community issues
     *
     * @param volleyError an object containg error details. See {@link VolleyError}
     */
    @Override
    public void onError(VolleyError volleyError) {
        Toast.makeText(requireActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
    }


    /**
     * A mothod invoked when a get Issues request is successful
     *
     * @param response A JSONObject containing a a list of community issues
     *
     * Configures the adapter and recycler view for the community issues
     */
    @Override
    @SneakyThrows
    public void onResponse(JSONObject response) {
        JSONArray issuesArray = response.getJSONObject("_embedded").getJSONArray("issues");
        numberOfPages = response.getJSONObject("page").getInt("totalPages");
        if (issuesArray.length() == 0) {
            noIssuesMessage.setVisibility(View.VISIBLE);
        } else {
            for (int x = 0; x < issuesArray.length(); x++) {
                JSONObject issueObject = response.getJSONObject("_embedded")
                        .getJSONArray("issues")
                        .getJSONObject(x);

                int issueLikes = issueObject.getInt("issueLikes");
                int issueDislikes = issueObject.getInt("issueDislikes");
                String imageUrl = null;
                if (!issueObject.getJSONObject("_links").isNull("imageUrl")) {
                    imageUrl = issueObject.getJSONObject("_links").getJSONObject("imageUrl").getString("href");
                }
                Issue issue = Issue.builder()
                        .uuid(issueObject.getString("uuid"))
                        .createdAt(issueObject.getString("createdAt"))
                        .createdBy(issueObject.getString("createdBy"))
                        .crop(issueObject.getString("crop"))
                        .issueLikes(issueLikes > 0 ? String.valueOf(issueLikes) : "like")
                        .issueDislikes(issueDislikes > 0 ? String.valueOf(issueDislikes) : "dislike")
                        .issueAnswers(issueObject.getInt("issueAnswers") + "")
                        .issueStatus(issueObject.getString("issueStatus"))
                        .question(issueObject.getString("question"))
                        .imageAvatarUrl(imageUrl)
                        .questionDescription(issueObject.getString("questionDescription"))
                        .build();
                issueList.add(issue);
            }
            //set up the adapter
            adapter = new IssuesRecyclerViewAdapter(requireContext(), issueList);
            LinearLayoutManager r = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(r);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            etSearch.addTextChangedListener(new TextValidator(etSearch) {
                @Override
                public void validate() {
                    if (etSearch.getText().toString().length() == 0) {
                        adapter.searchIssue("");
                    } else {
                        String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                        adapter.searchIssue(text);
                    }
                }
            });
        }


    }

    /**
     * Shows the progress of get Issues request
     */
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }


    /**
     * Hides the progress of get Issues request
     *
     * Invoked when there is an error or the Get Issues request is successful
     */
    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


    /**
     * Performing a clean sleet for the fragment
     *
     * Releasing all the resources held by the fragment before detaching it
     */
    @Override
    public void onDetach() {
        super.onDetach();
        VolleyController.getInstance(requireContext()).getRequestQueue().cancelAll(AppConstants.GET_COMMUNITY_ISSUES);
        VolleyController.getInstance(requireContext()).getRequestQueue().cancelAll("send_resolve_issue");

    }
}