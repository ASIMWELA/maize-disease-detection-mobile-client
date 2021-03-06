package com.detection.diseases.maize.usecases.account;

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
import com.detection.diseases.maize.usecases.signin.LoggedInUserModel;
import com.detection.diseases.maize.usecases.signin.SigninActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Augustine
 *
 * Manages all tasks associated with user account
 *
 */
public class AccountActivity extends Fragment {
    /**
     * A helper class which manages user session of the application
     */
    private SessionManager sessionManager;

    /**
     * Initiates opening the login activity if a user is logged out
     */
    private Button openLoginActivity;

    /**
     * A Custom back icon for fragment without an action bar
     */
    private Chip backIcon;

    /**
     * TextViews for a logged in user to display:
     *
     * tvUsername: logged in user
     *
     * tvEmail: A Logged user email
     */
    private TextView tvUsername, tvEmail;

    /**
     * A Layout view for a logged in user and a logged out user.
     *
     * They are swapped based on the user status
     */
    private ConstraintLayout loggedOutUserView, loggedInUserView;

    /**
     * TabLayout to hold logged in user tabs for involvement and about
     */
    private TabLayout loggedInTabs;

    /**
     * View pager responsible for managing logged in user
     */
    private ViewPager loggedInUserTabViewPager;
    private final Gson gson = new Gson();
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

        if (checkUserSession()) {
            ivOPenMenuVertLoggedoutUser.setVisibility(View.GONE);
            ivOpenMenuVertLoggedInUser.setVisibility(View.VISIBLE);
        }
        loggedOutUserMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.about_app_logged_out_user) {
                Toast.makeText(requireActivity(), "Logged out user", Toast.LENGTH_SHORT).show();
                return true;
            }
            return true;
        });

        loggedInUserMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.log_out_user_account) {
                loggedInUserView.setVisibility(View.GONE);
                loggedOutUserView.setVisibility(View.VISIBLE);
                sessionManager.setAccessToken(null);
                sessionManager.setLoggedInUser(null);
                ivOPenMenuVertLoggedoutUser.setVisibility(View.VISIBLE);
                ivOpenMenuVertLoggedInUser.setVisibility(View.GONE);
                return true;
            }
            return true;
        });

        ivOpenMenuVertLoggedInUser.setOnClickListener(v -> {
            loggedInUserMenu.show();
            loggedInUserMenu.setForceShowIcon(true);
        });
        ivOPenMenuVertLoggedoutUser.setOnClickListener(v -> {
            loggedOutUserMenu.show();
        });


        return root;
    }

    /**
     * Initialise views of this class
     *
     * @param root The base view of this fragment
     */
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
        ivOPenMenuVertLoggedoutUser = root.findViewById(R.id.iv_fg_user_account_logged_out_vert);
        ivLoggedInUser = root.findViewById(R.id.iv_logged_in_auga);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkUserSession()) {
            loggedInUserView.setVisibility(View.VISIBLE);
            loggedOutUserView.setVisibility(View.GONE);
            LoggedInUserModel userModel = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
            tvEmail.setText(userModel.getEmail());
            tvUsername.setText(userModel.getFirstName() + " " + userModel.getLastName());
            ivOPenMenuVertLoggedoutUser.setVisibility(View.GONE);
            ivOpenMenuVertLoggedInUser.setVisibility(View.VISIBLE);
            if (userModel.getEmail().equals("auga@gmail.com")) {
                Picasso.get().load(R.drawable.auga_disp).fit().centerCrop().into(ivLoggedInUser);
            }else {
                ivLoggedInUser.setImageResource(R.drawable.ic_baseline_person_24_anonymous);
            }
        }

    }

    /**
     * Check if the user is logged in or not
     *
     * @return true if user is logged in else false
     */
    public boolean checkUserSession() {
        return sessionManager.getLoggedInUser() != null && sessionManager.getToken() != null;
    }

}