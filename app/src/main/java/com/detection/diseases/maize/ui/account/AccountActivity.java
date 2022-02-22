package com.detection.diseases.maize.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.ui.signin.SigninActivity;

public class AccountActivity extends Fragment {

    private SessionManager sessionManager;
    private Button openLoginActivity, logout;
    private ConstraintLayout loggedOutUserView, loggedInUserView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_not_logged_in_user, container, false);
        sessionManager = new SessionManager(requireContext());


        loggedOutUserView = root.findViewById(R.id.activity_user_home_logged_out_user_view);
        loggedInUserView = root.findViewById(R.id.account_user_logged_in_view);
        openLoginActivity = root.findViewById(R.id.account_activity_login_btn);


        if(sessionManager.getLoggedInUser() != null && sessionManager.getToken() != null){
            loggedInUserView.setVisibility(View.VISIBLE);
            loggedOutUserView.setVisibility(View.GONE);
        }

        logout = root.findViewById(R.id.actitivity_logout);
           logout.setOnClickListener(v->{
               sessionManager.setUserRole(null);
               sessionManager.setLoggedInUser(null);
               sessionManager.setAccessToken(null);
               loggedInUserView.setVisibility(View.GONE);
               loggedOutUserView.setVisibility(View.VISIBLE);
           });

            openLoginActivity.setOnClickListener(v->{
                Intent intent = new Intent(requireContext(), SigninActivity.class);
                startActivity(intent);
            });





        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(sessionManager.getLoggedInUser() != null && sessionManager.getToken() != null){
            loggedInUserView.setVisibility(View.VISIBLE);
            loggedOutUserView.setVisibility(View.GONE);
        }
    }
}