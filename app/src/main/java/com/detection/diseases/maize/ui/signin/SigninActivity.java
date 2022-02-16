package com.detection.diseases.maize.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.signup.SignUpActivity;

public class SigninActivity extends Fragment {

    private SigninPresenter signinPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_signin, container, false);

        Button btn = root.findViewById(R.id.btn_opn_sing_up);
        btn.setOnClickListener(v->{
            Intent i = new Intent(requireActivity(), SignUpActivity.class);
            startActivity(i);
        });
        return root;
    }
}