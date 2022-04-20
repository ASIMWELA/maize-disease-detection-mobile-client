package com.detection.diseases.maize.usecases.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detection.diseases.maize.R;

/**
 * @author Augustine
 *
 * A fragment for an about section of a logged in user
 */
public class UserLoggedinAbout extends Fragment {


    /**
     * Called when the view gets inflated
     *
     * @param inflater Inflates the layout of this Fragment
     *
     * @param container The base view  group of the fragment
     *
     * @param savedInstanceState A Bundle for retrieving fragment's saved state
     *
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_loggedin_about, container, false);
    }
}