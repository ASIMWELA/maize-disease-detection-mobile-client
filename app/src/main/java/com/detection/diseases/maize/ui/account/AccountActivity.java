package com.detection.diseases.maize.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.ui.signin.LoggedInUserModel;
import com.detection.diseases.maize.ui.signin.SigninActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class AccountActivity extends Fragment {

    private SessionManager sessionManager;
    private Button openLoginActivity, logout;
    private Chip backIcon;
    private TextView tvUsername, tvEmail;
    private ConstraintLayout loggedOutUserView, loggedInUserView;
    private LoggedInUserModel userModel;
    TabLayout loggedInTabs;
    ViewPager loggedInUserTabViewPager;
    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_not_logged_in_user, container, false);
        initViews(root);


        sessionManager = new SessionManager(requireContext());
        if (sessionManager.getLoggedInUser() != null && sessionManager.getToken() != null) {
            loggedInUserView.setVisibility(View.VISIBLE);
            loggedOutUserView.setVisibility(View.GONE);
            userModel = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
            tvEmail.setText(userModel.getEmail());
            tvUsername.setText(userModel.getFirstName() + " " + userModel.getLastName());
        }

        openLoginActivity.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SigninActivity.class);
            startActivity(intent);
        });
        backIcon.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        loggedInTabs.addTab(loggedInTabs.newTab().setText("Involvement"));
        loggedInTabs.addTab(loggedInTabs.newTab().setText("About"));
        loggedInTabs.setTabGravity(TabLayout.GRAVITY_FILL);

        final FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), loggedInTabs.getTabCount());
        loggedInUserTabViewPager.setAdapter(adapter);
        loggedInUserTabViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(loggedInTabs));

        loggedInTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loggedInUserTabViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        return root;
    }

    private void initViews(View root) {
        backIcon = root.findViewById(R.id.user_account_back);
        loggedOutUserView = root.findViewById(R.id.user_logged_out_view);
        loggedInUserView = root.findViewById(R.id.logged_in_user_view);
        openLoginActivity = root.findViewById(R.id.account_activity_login_btn);
        tvUsername = root.findViewById(R.id.tv_logged_in_user_name);
        tvEmail = root.findViewById(R.id.tv_logged_in_user_email);
        loggedInTabs = root.findViewById(R.id.logged_in_user_tab_layout);
        loggedInUserTabViewPager = root.findViewById(R.id.logged_in_user_fragmentsPager);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.getLoggedInUser() != null && sessionManager.getToken() != null) {
            loggedInUserView.setVisibility(View.VISIBLE);
            loggedOutUserView.setVisibility(View.GONE);
            userModel = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
            tvEmail.setText(userModel.getEmail());
            tvUsername.setText(userModel.getFirstName() + " " + userModel.getLastName());
        }
    }

}