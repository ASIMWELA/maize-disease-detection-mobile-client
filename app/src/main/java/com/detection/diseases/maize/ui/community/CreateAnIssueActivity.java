package com.detection.diseases.maize.ui.community;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.RealPathUtil;
import com.detection.diseases.maize.ui.camera.CameraActivity;
import com.detection.diseases.maize.ui.community.model.GalleryImageModel;
import com.detection.diseases.maize.ui.modelresults.model.ImageIdsHolder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.SneakyThrows;

public class CreateAnIssueActivity extends AppCompatActivity {

    private static final String TAG = "CreateISsue";
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 200;
    ConstraintLayout bottomSheetView;
    BottomSheetBehavior<?> bottomSheetBehavior;
    ImageView ivUpDown;
    GridView imageGridContainer;
    List<File> images = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_issue);
        initViews();
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);


        if(checkAndRequestPermissions()){
            loadImagesAndAttachToAdapter();
        }





        ivUpDown.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        imageGridContainer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                ivUpDown.setRotation(slideOffset * 180);

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadImagesAndAttachToAdapter() {
        images.addAll(RealPathUtil.getAllShownImagesPath(this));
        List<GalleryImageModel> imodels = new ArrayList<>();
        images.forEach(r->{
            GalleryImageModel galleryImageModel = new GalleryImageModel(r);
            imodels.add(galleryImageModel);
        });
        GalleryImageAdapter gridAdapter = new GalleryImageAdapter(this, imodels);
        imageGridContainer.setAdapter(gridAdapter);
    }

    private void initViews() {
        bottomSheetView = findViewById(R.id.images_bottom_sheet_view);
        ivUpDown = findViewById(R.id.bttom_sheet_up_arrow);
        imageGridContainer = findViewById(R.id.bottom_sheet_images_grid);
    }

    public boolean checkAndRequestPermissions() {
        int extstorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (extstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded
                            .toArray(new String[0]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SneakyThrows
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(CreateAnIssueActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "The app need access to camera", Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else {
               loadImagesAndAttachToAdapter();
            }
        }

    }
}