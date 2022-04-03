package com.detection.diseases.maize.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.CustomOptionsMenu;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.ui.signin.LoggedInUserModel;
import com.detection.diseases.maize.ui.signin.SigninActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends Fragment {

    private SessionManager sessionManager;
    private Button openLoginActivity;
    private Chip backIcon;
    private TextView tvUsername, tvEmail;
    private ConstraintLayout loggedOutUserView, loggedInUserView;
    private LoggedInUserModel userModel;
    private TabLayout loggedInTabs;
    private ViewPager loggedInUserTabViewPager;
    private Gson gson = new Gson();
    private ImageView ivOpenMenuVertLoggedInUser, ivOPenMenuVertLoggedoutUser;
    private PopupMenu loggedInUserMenu;
    private PopupMenu loggedOutUserMenu;
    private CircleImageView ivLoggedInUser;

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_user_account_view, container, false);
        initViews(root);

        sessionManager = new SessionManager(requireContext());

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

        loggedInUserMenu = CustomOptionsMenu.prepareOptionsMenu(requireActivity(), ivOpenMenuVertLoggedInUser, R.menu.logged_in_user_more_menu);

        loggedOutUserMenu = CustomOptionsMenu.prepareOptionsMenu(requireActivity(), ivOPenMenuVertLoggedoutUser, R.menu.logged_out_user_more_menu);

        if(checkUserSession()){
            ivOPenMenuVertLoggedoutUser.setVisibility(View.GONE);
            ivOpenMenuVertLoggedInUser.setVisibility(View.VISIBLE);
        }
        loggedOutUserMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.about_app_logged_out_user:
                    Toast.makeText(requireActivity(), "Logged out user", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return true;
        });
        loggedInUserMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.log_out_user_account:
                    loggedInUserView.setVisibility(View.GONE);
                    loggedOutUserView.setVisibility(View.VISIBLE);
                    sessionManager.setAccessToken(null);
                    sessionManager.setLoggedInUser(null);
                    ivOPenMenuVertLoggedoutUser.setVisibility(View.VISIBLE);
                    ivOpenMenuVertLoggedInUser.setVisibility(View.GONE);
                    return true;
                case R.id.about_app:
                    Toast.makeText(requireContext(), "About logged in user", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return true;
        });

        ivOpenMenuVertLoggedInUser.setOnClickListener(v -> {
            loggedInUserMenu.show();
        });
        ivOPenMenuVertLoggedoutUser.setOnClickListener(v->{
            loggedOutUserMenu.show();
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
        ivOpenMenuVertLoggedInUser = root.findViewById(R.id.iv_fg_user_account_more_vert);
        ivOPenMenuVertLoggedoutUser =root.findViewById(R.id.iv_fg_user_account_logged_out_vert);
        ivLoggedInUser = root.findViewById(R.id.iv_logged_in_auga);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkUserSession()) {
            loggedInUserView.setVisibility(View.VISIBLE);
            loggedOutUserView.setVisibility(View.GONE);
            userModel = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
            tvEmail.setText(userModel.getEmail());
            tvUsername.setText(userModel.getFirstName() + " " + userModel.getLastName());
            ivOPenMenuVertLoggedoutUser.setVisibility(View.GONE);
            ivOpenMenuVertLoggedInUser.setVisibility(View.VISIBLE);
            if(userModel.getEmail().equals("auga@gmail.com")){
                Picasso.get().load(R.drawable.auga_disp).fit().centerCrop().into(ivLoggedInUser);
            }
            }

    }

    public boolean checkUserSession() {
        return sessionManager.getLoggedInUser() != null && sessionManager.getToken() != null;
    }

}