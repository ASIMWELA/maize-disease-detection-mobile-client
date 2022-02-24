package com.detection.diseases.maize.ui.camera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Size;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.HttpAsyncHelper;
import com.detection.diseases.maize.helpers.RealPathUtil;
import com.detection.diseases.maize.ui.modelresults.ModelResultsActivity;
import com.google.common.util.concurrent.ListenableFuture;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import lombok.SneakyThrows;

public class CameraActivity extends AppCompatActivity implements CameraActivityContract.View {
    private static final int SELECT_IMAGE_CODE = 2;
    private PreviewView cameraPreview;
    private ImageView ivCaptureImage, ivLoadGallery, ivShowHint, previewCapturedImage;
    private ImageButton sendImage;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private ImageCapture imageCapture;
    private CameraActivityPresenter cameraActivityPresenter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    String captureImageUrl = null;
    private ConstraintLayout sendOveray;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    @SneakyThrows
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initViews();
        cameraActivityPresenter = new CameraActivityPresenter(this, this);

        if (checkAndRequestPermissions()) {
            cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderListenableFuture.addListener(() -> {
                ProcessCameraProvider cameraProvider = null;
                try {
                    cameraProvider = cameraProviderListenableFuture.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assert cameraProvider != null;
                startCamera(cameraProvider);
            }, getExecutor());
        } else {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        ivCaptureImage.setOnClickListener(v -> cameraActivityPresenter.capturePictureBtnClicked());

        sendImage.setOnClickListener(v -> cameraActivityPresenter.uploadFile(new File(captureImageUrl)));

        ivLoadGallery.setOnClickListener(v -> cameraActivityPresenter.loadGalleyBtnClicked());

    }

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(CameraActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "The app need access to camera", Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else if (ContextCompat.checkSelfPermission(CameraActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "The app need access to storage",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                ProcessCameraProvider cameraProvider = null;
                try {
                    cameraProvider = cameraProviderListenableFuture.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startCamera(cameraProvider);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        cameraPreview = findViewById(R.id.camera_preview);
        ivCaptureImage = findViewById(R.id.camera_activity_iv_capture_image);
        ivLoadGallery = findViewById(R.id.camera_activity_iv_load_galley);
        ivShowHint = findViewById(R.id.camera_activity_iv_show_hint);
        sendImage = findViewById(R.id.camera_activity_send_image);
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        previewCapturedImage = findViewById(R.id.iv_camera_preview_capture_image);
        sendOveray = findViewById(R.id.camera_activity_overalay);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void capturePicture() {
        File photoDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Maize disease");
        if (!photoDir.exists()) {
            photoDir.mkdir();
        }
        Date date = new Date();
        String timeStamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + "IMG_" + timeStamp + ".jpg";
        File photoFile = new File(photoFilePath);
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        captureImageUrl = outputFileResults.getSavedUri().getPath();
                        Picasso.get().load(outputFileResults.getSavedUri()).fit().into(previewCapturedImage);
                        previewCapturedImage.setVisibility(View.VISIBLE);
                        ivCaptureImage.setVisibility(View.GONE);
                        ivLoadGallery.setVisibility(View.GONE);
                        ivShowHint.setVisibility(View.GONE);
                        sendImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }

    @Override
    public void showHint() {


    }

    @Override
    public void loadGalley() {
        Intent selectImage = new Intent();
        selectImage.setType("image/*");
        selectImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(selectImage, SELECT_IMAGE_CODE);
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void startCamera(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setMaxResolution(new Size(950, 540))
                .setJpegQuality(100)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);

    }

    @Override
    public void showProgressBar() {
        sendImage.setClickable(false);
        sendOveray.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        sendImage.setClickable(true);
        sendOveray.setVisibility(View.GONE);
    }

    @Override
    public void onUploadSucess(String response) {
        Intent intent = new Intent(this, ModelResultsActivity.class);
        intent.putExtra(AppConstants.MODEL_RESULTS, response);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUploadError(String errorResponse) {
        Toast.makeText(this, "Error" + errorResponse, Toast.LENGTH_SHORT).show();
    }

    public Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }


    public boolean checkAndRequestPermissions() {
        int extstorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (extstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded
                            .toArray(new String[0]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (previewCapturedImage.getVisibility() == View.VISIBLE) {
            previewCapturedImage.setVisibility(View.GONE);
            sendImage.setVisibility(View.GONE);
            ivCaptureImage.setVisibility(View.VISIBLE);
            ivShowHint.setVisibility(View.VISIBLE);
            ivLoadGallery.setVisibility(View.VISIBLE);
            sendOveray.setVisibility(View.GONE);
            HttpAsyncHelper.getInstance(this).getHttpClient().cancelRequestsByTAG(AppConstants.POST_IMAGE_TAG_REQUEST, true);
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            Uri galleyUri = data.getData();
            Picasso.get().load(galleyUri).fit().into(previewCapturedImage);
            previewCapturedImage.setVisibility(View.VISIBLE);
            ivCaptureImage.setVisibility(View.GONE);
            ivLoadGallery.setVisibility(View.GONE);
            ivShowHint.setVisibility(View.GONE);
            sendImage.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT < 19) {
                captureImageUrl = RealPathUtil.getRealPathFromURI_API11to18(this, galleyUri);
            } else {
                captureImageUrl = RealPathUtil.getRealPathFromURI_API19(this, galleyUri);
            }

        }

    }

}