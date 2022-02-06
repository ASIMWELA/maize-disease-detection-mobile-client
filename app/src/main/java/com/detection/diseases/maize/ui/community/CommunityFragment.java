package com.detection.diseases.maize.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.community.payload.Issue;
import com.google.android.material.chip.Chip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment implements CommunityContract.View{

    private CommunityPresenter communityPresenter;
    private RecyclerView recyclerView;
    private EditText etSearch;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private Chip askCommunity;
    private ConstraintLayout communityRootView;
    private IssuesRecyclerViewAdapter adapter;
    List<Issue> issueList;
    private int page = 0, numberOfPages=1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_community, container, false);
        initComponent(root);
        communityPresenter.getCommunityIssues(page, numberOfPages);

//        nestedScrollView.setNestedScrollingEnabled(true);
//        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    // in this method we are incrementing page number,
//                    // making progress bar visible and calling get data method.
//                    page++;
//                    communityPresenter.getCommunityIssues(page, numberOfPages);
//                }
//            }
//        });
        return root;
    }

    private void initComponent(View root) {
        recyclerView = root.findViewById(R.id.fg_community_issues_rv);
        etSearch = root.findViewById(R.id.fg_community_search_issue_tv);
        nestedScrollView = (NestedScrollView) root.findViewById(R.id.fg_community_nsc_view);
        progressBar = root.findViewById(R.id.fg_community_pagination_pb);
        askCommunity = root.findViewById(R.id.fg_community_ask_cp);
        communityRootView = root.findViewById(R.id.fg_community_root_view);
        issueList = new ArrayList<>();
        communityPresenter = new CommunityPresenter(this, requireContext());
    }

    @Override
    public void onError(VolleyError volleyError) {
        Toast.makeText(requireContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewDestroyed() {


    }

    @Override
    public void onResponse(JSONObject response) {
        numberOfPages = 10;
        Toast.makeText(requireContext(), response.toString(), Toast.LENGTH_SHORT).show();
        adapter = new IssuesRecyclerViewAdapter(requireContext(), issueList);
        LinearLayoutManager r = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(r);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}