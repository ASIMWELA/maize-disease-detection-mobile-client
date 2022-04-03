package com.detection.diseases.maize.ui.community;

import android.content.Context;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.TextValidator;
import com.detection.diseases.maize.helpers.VolleyController;
import com.detection.diseases.maize.ui.community.payload.Issue;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;

public class CommunityFragment extends Fragment implements CommunityContract.View {

    private CommunityPresenter communityPresenter;
    private RecyclerView recyclerView;
    private EditText etSearch;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private Chip askCommunity;
    private ConstraintLayout communityRootView;
    private IssuesRecyclerViewAdapter adapter;
    List<Issue> issueList;
    private TextView noIssuesMessage;
    private int page = 0, numberOfPages = 1;
    private Chip chBackChip;

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





//        swipeRefreshLayout.setOnRefreshListener(() -> {
//            issueList.clear();
//            communityPresenter.getCommunityIssues(page,numberOfPages);
//        });

        return root;
    }

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

        //set up the adapter
        adapter = new IssuesRecyclerViewAdapter(requireContext(), issueList);
        LinearLayoutManager r = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(r);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(VolleyError volleyError) {
        Toast.makeText(requireActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewDestroyed() {


    }

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
                        .issueAnswers(issueObject.getInt("issueAnswers")==1?issueObject.getInt("issueAnswers") + " answer" : issueObject.getInt("issueAnswers") + " answers")
                        .issueStatus(issueObject.getString("issueStatus"))
                        .question(issueObject.getString("question"))
                        .imageAvatarUrl(imageUrl)
                        .questionDescription(issueObject.getString("questionDescription"))
                        .build();
                issueList.add(issue);
            }
            adapter.notifyDataSetChanged();
        }

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

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        VolleyController.getInstance(requireContext()).getRequestQueue().cancelAll(AppConstants.GET_COMMUNITY_ISSUES);
        VolleyController.getInstance(requireContext()).getRequestQueue().cancelAll("send_resolve_issue");

    }

    @Override
    public void stopReshresh() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}