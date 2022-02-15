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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.signup.SignUpActivity;

public class AccountFragment extends Fragment {

    private AccountPresenter accountPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        Button btn = root.findViewById(R.id.btn_opn_sing_up);
        btn.setOnClickListener(v->{
            Intent i = new Intent(requireActivity(), SignUpActivity.class);
            startActivity(i);
        });
        return root;
    }
}