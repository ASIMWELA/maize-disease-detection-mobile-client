package com.detection.diseases.maize.ui.community;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.RealPathUtil;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.helpers.TextValidator;
import com.detection.diseases.maize.ui.community.model.CropsModel;
import com.detection.diseases.maize.ui.community.model.GalleryImageModel;
import com.detection.diseases.maize.ui.community.payload.CropsGridAdapter;
import com.detection.diseases.maize.ui.signin.LoggedInUserModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import lombok.SneakyThrows;

public class CreateAnIssueActivity extends AppCompatActivity implements CreateIssueContract.View {
    private static final String TAG = "CreateISsue";
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 200;
    private ConstraintLayout bottomSheetView;
    private BottomSheetBehavior<?> bottomSheetBehavior;
    private ImageView ivUpDown, iVSelectedImage, ivCancelImageSelection, ivOpenSelectImageTray;
    private GridView imageGridContainer;
    private final List<File> images = new ArrayList<>();
    private CircularProgressButton btnCreateIssue;
    private EditText edIssueQuestion, edIssueDescription;
    private File fSelectedImage;
    private Chip cpBackArrow;
    private String crop, question, questionDescription;
    private RequestParams data = new RequestParams();
    private Gson gson = new Gson();
    private Button btnOpenCropsDialog;
    private TextView tvDisplaySeletedCrop;
    private CreateIssuePresenter createIssuePresenter;
    private String token;
    private LoggedInUserModel loggedInUserModel;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_issue);
        initViews();
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        createIssuePresenter = new CreateIssuePresenter(this, this);
        SessionManager sessionManager = new SessionManager(this);
        loggedInUserModel = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
        token = sessionManager.getToken();

        if (checkAndRequestPermissions()) {
            loadImagesAndAttachToAdapter();
        }

        btnOpenCropsDialog.setOnClickListener(v->{
            buildDialog().show();
        });
        cpBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
        ivUpDown.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setDraggable(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        btnCreateIssue.setOnClickListener(v -> {
            //btnCreateIssue.;
        });
        edIssueQuestion.setOnFocusChangeListener((view, b) -> {
            if (b) {
                bottomSheetBehavior.setHideable(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        edIssueDescription.setOnFocusChangeListener((view, b) -> {
            if (b) {
                bottomSheetBehavior.setHideable(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        imageGridContainer.setOnItemClickListener((adapterView, view, position, id) -> {
            GalleryImageModel selectedImage = (GalleryImageModel) adapterView.getItemAtPosition(position);
            fSelectedImage = selectedImage.getImage();
            Picasso.get().load(fSelectedImage).fit().centerCrop().into(iVSelectedImage);
            iVSelectedImage.setVisibility(View.VISIBLE);
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            ivCancelImageSelection.setVisibility(View.VISIBLE);

        });

        ivOpenSelectImageTray.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.setDraggable(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


        ivCancelImageSelection.setOnClickListener(v -> {
            ivCancelImageSelection.setVisibility(View.GONE);
            iVSelectedImage.setVisibility(View.GONE);
            iVSelectedImage.setImageDrawable(null);
            bottomSheetBehavior.setHideable(false);
            bottomSheetBehavior.setDraggable(true);
            edIssueDescription.clearFocus();
            edIssueQuestion.clearFocus();
            fSelectedImage = null;
        });


        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setDraggable(false);
                }
            }

            //change the state of the arrow icon
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                ivUpDown.setRotation(slideOffset * 180);
            }
        });

        tvDisplaySeletedCrop.setOnClickListener(v->{
            crop = null;
            tvDisplaySeletedCrop.setVisibility(View.GONE);
            btnOpenCropsDialog.setVisibility(View.VISIBLE);
            btnOpenCropsDialog.setEnabled(true);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadImagesAndAttachToAdapter() {
        images.addAll(RealPathUtil.getAllShownImagesPath(this));
        List<GalleryImageModel> imodels = new ArrayList<>();
        images.forEach(r -> {
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
        btnCreateIssue = findViewById(R.id.create_issue_btn_send_issue);
        edIssueQuestion = findViewById(R.id.edt_create_issue_question);
        edIssueDescription = findViewById(R.id.ed_create_issue_question_desc);
        iVSelectedImage = findViewById(R.id.iv_create_an_issue_selected_image);
        ivCancelImageSelection = findViewById(R.id.iv_cancel_image_selectiton);
        cpBackArrow = findViewById(R.id.create_issue_back);
        ivOpenSelectImageTray = findViewById(R.id.iv_open_select_image_tray);
        btnOpenCropsDialog = findViewById(R.id.btn_open_select_crop_dialog);
        tvDisplaySeletedCrop = findViewById(R.id.tv_display_selected_crop);
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

    @Override
    public boolean validateInputs() {
        edIssueQuestion.addTextChangedListener(new TextValidator(edIssueQuestion) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            @SneakyThrows
            public void validate() {
                if (!edIssueQuestion.getText().toString().isEmpty()) {
                    if (edIssueQuestion.getText().toString().trim().length() < 3) {
                        edIssueQuestion.setError("first name too short");
                        question = null;
                    } else {
                        question = edIssueQuestion.getText().toString().trim();
                    }
                } else {
                    edIssueQuestion.setError(null);
                }
            }
        });
        edIssueDescription.addTextChangedListener(new TextValidator(edIssueDescription) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            @SneakyThrows
            public void validate() {
                if (!edIssueDescription.getText().toString().isEmpty()) {
                    if (edIssueDescription.getText().toString().trim().length() < 3) {
                        edIssueDescription.setError("first name too short");
                        question = null;
                    } else {
                        questionDescription = edIssueDescription.getText().toString().trim();
                    }
                } else {
                    edIssueQuestion.setError(null);
                }
            }
        });
        return question != null
                && questionDescription != null
                && crop != null;

    }

    @Override
    public void onValidationFailure() {
        Toast.makeText(this, "Please fill the\nrequired fields", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendIssueSuccess(String response) {

    }

    @Override
    public void onSendIssueError(String errorResponse) {

    }

    @Override
    public void showProgress() {
        btnCreateIssue.startAnimation();
    }

    @Override
    public void hideProgress() {
        btnCreateIssue.stopAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnCreateIssue.dispose();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private AlertDialog buildDialog() {
        AlertDialog.Builder searchDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.crops_holder_layout, null);
        searchDialog.setView(dialogView);
        GridView cropsGrid = dialogView.findViewById(R.id.crops_holder_grid);

        List<CropsModel> cropsModels = Stream.of(
                CropsModel.builder()
                        .cropName("Maize")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Cassava")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Millet")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tobacco")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tomato")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Apple")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Bean")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Cabbage")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Coffee")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Ginger")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Mango")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Melon")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Papaya")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Banana")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Onion")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tea")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Other")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Banana")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Onion")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tea")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Other")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Banana")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Onion")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tea")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Other")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Banana")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Onion")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tea")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Other")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Banana")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Onion")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tea")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Other")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Banana")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Onion")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Tea")
                        .imageId(R.drawable.common_rust_3)
                        .build(),
                CropsModel.builder()
                        .cropName("Other")
                        .imageId(R.drawable.common_rust_3)
                        .build()
        ).collect(Collectors.toList());

        AlertDialog alertDialog = searchDialog.create();

        alertDialog.setCanceledOnTouchOutside(false);

        CropsGridAdapter cropsGridAdapter = new CropsGridAdapter(this, cropsModels);
        cropsGrid.setAdapter(cropsGridAdapter);
        cropsGrid.setOnItemClickListener((adapterView, view, position, id) -> {
            CropsModel cropModel = cropsModels.get(position);
            crop = cropModel.getCropName();
            tvDisplaySeletedCrop.setText(crop);
            tvDisplaySeletedCrop.setVisibility(View.VISIBLE);
            btnOpenCropsDialog.setVisibility(View.INVISIBLE);
            btnOpenCropsDialog.setEnabled(false);
            alertDialog.dismiss();
        });
        return alertDialog;
    }
}