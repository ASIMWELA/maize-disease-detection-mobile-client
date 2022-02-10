package com.detection.diseases.maize.ui.crops;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.camera.CameraActivity;

public class CropsFragment extends Fragment implements CropFragmentContract.View{

    private CropsPresenter cropsPresenter;
    private AppCompatButton btnOpenCamera;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crops, container, false);
        btnOpenCamera = root.findViewById(R.id.fg_crops_btn_open_camera);
        cropsPresenter = new CropsPresenter(this);
        btnOpenCamera.setOnClickListener(v->cropsPresenter.openCameraBtnClicked());
        return root;
    }

    @Override
    public void onOpenCamera() {
        Intent intent = new Intent(requireActivity(), CameraActivity.class);
        startActivity(intent);
    }
}