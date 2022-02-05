package com.detection.diseases.maize.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.detection.diseases.maize.R;

public class CommunityFragment extends Fragment {

    private CommunityPresenter communityPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        communityPresenter =
                new ViewModelProvider(this).get(CommunityPresenter.class);
        View root = inflater.inflate(R.layout.fragment_community, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        communityPresenter.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}