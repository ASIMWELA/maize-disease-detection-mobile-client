package com.detection.diseases.maize.ui.account;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.ui.signin.LoggedInUserModel;
import com.google.gson.Gson;

/**
 * @author Augustine
 *
 * This fragment contains logic for managing user involveement in the community section
 *
 */
public class UserLoggedInInvolvement extends Fragment {

    /**
     * Session Manager for a logged in user.
     *
     * Retrieves a logged in user from session
     */
    private SessionManager sessionManager;

    /**
     * A view for displaying a loged in user in the user involvement fragment
     */
    private TextView tvUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_logged_in_involvement, container, false);
        tvUserName = view.findViewById(R.id.tv_loggen_in_user_inv_name);

        sessionManager = new SessionManager(requireContext());
        return view;
    }

    /**
     * Set Text on the tvUserName text
     *
     *
     */
    @Override
    @SuppressLint("SetTextI18n")
    public void onResume() {
        super.onResume();
        Gson gson = new Gson();
        LoggedInUserModel user = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
        tvUserName.setText("Hie "+ user.getFirstName() +" "+ user.getLastName()+", Get involved in the " +
                "community Section to unlock new badges. " +
                "Suggest solutions, Create issues, " +
                "like, dislike and many more! ");
    }
}