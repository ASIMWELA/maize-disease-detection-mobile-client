package com.detection.diseases.maize.ui.community;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.CheckUserSession;
import com.detection.diseases.maize.helpers.RealPathUtil;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.helpers.TextValidator;
import com.detection.diseases.maize.ui.community.model.CropsModel;
import com.detection.diseases.maize.ui.community.model.GalleryImageModel;
import com.detection.diseases.maize.ui.signin.LoggedInUserModel;
import com.detection.diseases.maize.ui.signin.SigninActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import lombok.SneakyThrows;

/**
 * @author Augustine
 *
 * An activity responsible for creating issues in the community
 *
 */
public class CreateAnIssueActivity extends AppCompatActivity implements CreateIssueContract.View {
    /**
     * A unique code responsible for requesting permission to read external storage
     */
    private static final int REQUEST_READ_EXTERNAL_STORAGE_FOR_GETTING_ISSUE_IMAGE = 200;

    /**
     * A constraint view with a bottom sheet behaviour anabled
     */
    private ConstraintLayout bottomSheetView;

    /**
     * A Bottom sheet behaviour responsible for hiding an issue image when the activity is scrolled upward
     *
     *
     */
    private BottomSheetBehavior<?> bottomSheetBehavior;

    /**
     * ImageViews for holding icons for:
     *
     *  upDown: sets bottom sheet to slide up or down
     *
     *  SelectedImage: Previews a selected image for creating an issue
     *
     *  OpenSelectImageTray: Opens a bottom sheet when it was a hide mode
     */
    private ImageView ivUpDown, iVSelectedImage, ivOpenSelectImageTray;

    /**
     * Reverts a selected image for an issue
     *
     * Allows an instant reversible action on selecting an image for an issue
     */
    private CircleImageView ivCancelImageSelection;

    /**
     * Displaying loaded gallery images into a grid view and attaching it to a bottom sheet
     */
    private GridView imageGridContainer;
    /**
     * A List representing all gallery images
     */
    private final List<File> images = new ArrayList<>();

    /**
     * Triggers the send request for an issue
     */
    private CircularProgressButton btnCreateIssue;

    /**
     * Edit txts for getting user inputs
     *
     * edIssueQuestion: Get an issue question
     *
     * edIssueDescription: Get the question description
     */
    private EditText edIssueQuestion, edIssueDescription;

    /**
     * A file that holds a selected file for an issue.
     */
    private File fSelectedImage;

    /**
     * A custom back arrow that allows back press on a non toolbar fragment
     */
    private Chip cpBackArrow;

    /**
     * Strings holding values for issues
     *
     * crop: Crop name
     *
     * question: Question of the issue. From getText().toString() from edIssueQuestion
     */
    private String crop, question, questionDescription;

    /**
     * Used to get a user from session by directly converting from a string into a {@link LoggedInUserModel}
     */
    private final Gson gson = new Gson();

    /**
     * Btn for opening a dialog box for selecting a crop name
     */
    private Button btnOpenCropsDialog;

    /**
     * A Textview for displaying a selected crop
     */
    private TextView tvDisplaySeletedCrop;

    /**
     * A presenter for updating the view
     * @see CreateIssuePresenter
     */
    private CreateIssuePresenter createIssuePresenter;

    /**
     * An authorization token for an authorised user
     */
    private String token;

    /**
     * A logged in user object
     */
    private LoggedInUserModel loggedInUserModel;

    /**
     * A Json obect to hold the data to send to a remote API
     */
    JSONObject stringData = new JSONObject();

    /**
     * Creates an activity
     *
     * @param savedInstanceState a bundle containing a previous saved state
     */
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

        btnOpenCropsDialog.setOnClickListener(v -> {
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
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = this.getCurrentFocus();
                if (view == null) {
                    view = new View(this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

        tvDisplaySeletedCrop.setOnClickListener(v -> {
            crop = null;
            tvDisplaySeletedCrop.setVisibility(View.GONE);
            btnOpenCropsDialog.setVisibility(View.VISIBLE);
            btnOpenCropsDialog.setEnabled(true);
        });

        //set the initail validation
        createIssuePresenter.initValidation();

        btnCreateIssue.setOnClickListener(v -> {
            if (!CheckUserSession.isUserLoggedIn(this)) {
                Intent i = new Intent(this, SigninActivity.class);
                startActivity(i);
                Toast.makeText(this, "Login to take an action", Toast.LENGTH_SHORT).show();
            } else {
                RequestParams data = new RequestParams();

                try {   data.put("image", fSelectedImage);
                        data.put("issue", stringData.toString());
                        createIssuePresenter.sendIssueRequest(data, loggedInUserModel.getUuid(), token);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        });

        //get image from the camera activity
        String imageUrl = getIntent().getStringExtra(AppConstants.CAPTURED_IMAGE_URL);
        if(imageUrl != null){
            fSelectedImage = new File(imageUrl);
            Picasso.get().load(fSelectedImage).fit().centerCrop().into(iVSelectedImage);
            iVSelectedImage.setVisibility(View.VISIBLE);
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            ivCancelImageSelection.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets up loaded gallery images into an adapter
     */
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

    /**
     * Initialise all views of the activity
     */
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

    /**
     * Request permissions from the user
     *
     * @return true of granted false otherwise
     */
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
                    REQUEST_READ_EXTERNAL_STORAGE_FOR_GETTING_ISSUE_IMAGE);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SneakyThrows
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_FOR_GETTING_ISSUE_IMAGE) {
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

    /**
     * Validate all EditText input
     *
     * @return true if all are valid, false otherwise
     */

    @Override
    public boolean validateInputs() {
        edIssueQuestion.addTextChangedListener(new TextValidator(edIssueQuestion) {
            @Override
            @SneakyThrows
            public void validate() {
                if (!edIssueQuestion.getText().toString().isEmpty()) {
                    if (edIssueQuestion.getText().toString().trim().length() < 8) {
                        edIssueQuestion.setError("Question too short");
                        question = null;
                    } else {
                        question = edIssueQuestion.getText().toString().trim();
                        stringData.put("question", question);
                    }
                } else {
                    edIssueQuestion.setError(null);
                }
            }
        });
        edIssueDescription.addTextChangedListener(new TextValidator(edIssueDescription) {
            @Override
            @SneakyThrows
            public void validate() {
                if (!edIssueDescription.getText().toString().isEmpty()) {
                    if (edIssueDescription.getText().toString().trim().length() < 10) {
                        edIssueDescription.setError("Try to be brief !");
                        questionDescription = null;
                    } else {
                        questionDescription = edIssueDescription.getText().toString().trim();
                        stringData.put("questionDescription", questionDescription);
                    }
                } else {
                    edIssueQuestion.setError(null);
                }
            }
        });
        return question != null
                && questionDescription != null
                && crop != null
                && fSelectedImage != null;
    }

    /**
     * Invocked when there are some invalid inputs
     */
    @Override
    public void onValidationFailure() {
             Toast.makeText(this, "Please fill all the\nrequired fields and attach\nan image", Toast.LENGTH_SHORT).show();
    }

    /**
     * Invoked when send issue request  is successful
     *
     * @param response A stringified object of issues
     */
    @Override
    public void onSendIssueSuccess(String response) {
        crop = null;
        question = null;
        questionDescription=null;
        fSelectedImage=null;
        tvDisplaySeletedCrop.setVisibility(View.GONE);
        btnOpenCropsDialog.setVisibility(View.VISIBLE);
        ivCancelImageSelection.setVisibility(View.GONE);
        edIssueDescription.setText("");
        edIssueQuestion.setText("");
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        iVSelectedImage.setVisibility(View.GONE);
        Toast.makeText(this, "Issue Created", Toast.LENGTH_SHORT).show();
    }

    /**
     * Invoked when send Issues request results into an error
     *
     * @param errorResponse A String fied error object
     */
    @Override
    public void onSendIssueError(String errorResponse) {
        Toast.makeText(this, "Error: Unable to create issue", Toast.LENGTH_SHORT).show();
    }

    /**
     * Show send Issue request progress
     */
    @Override
    public void showProgress() {
        btnCreateIssue.startAnimation();
    }

    /**
     * hide send Issue request progress
     */
    @Override
    public void hideProgress() {
        btnCreateIssue.setBackgroundResource(R.drawable.round_btn_bg);
        btnCreateIssue.revertAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnCreateIssue.dispose();
    }

    /**
     * Build an an alert dialog for holding crops
     *
     * @return {@link AlertDialog}
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SneakyThrows
    private AlertDialog buildDialog() {
        AlertDialog.Builder searchDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.crops_holder_layout, null);
        searchDialog.setView(dialogView);
        GridView cropsGrid = dialogView.findViewById(R.id.crops_holder_grid);
        EditText edSearchCrop = dialogView.findViewById(R.id.ed_search_crop);

        List<CropsModel> cropsModels = Stream.of(
                CropsModel.builder()
                        .cropName("Apple")
                        .imageId(R.drawable.apple)
                        .build(),
                CropsModel.builder()
                        .cropName("Cashew nut")
                        .imageId(R.drawable.cashew)
                        .build(),
                CropsModel.builder()
                        .cropName("Avocado")
                        .imageId(R.drawable.avocado)
                        .build(),
                CropsModel.builder()
                        .cropName("Beans")
                        .imageId(R.drawable.bean)
                        .build(),
                CropsModel.builder()
                        .cropName("Cabbage")
                        .imageId(R.drawable.cabbage)
                        .build(),
                CropsModel.builder()
                        .cropName("Carrot")
                        .imageId(R.drawable.carrot)
                        .build(),
                CropsModel.builder()
                        .cropName("Cassava")
                        .imageId(R.drawable.cassava)
                        .build(),
                CropsModel.builder()
                        .cropName("Cherry")
                        .imageId(R.drawable.cherry)
                        .build(),
                CropsModel.builder()
                        .cropName("Chinese leaves")
                        .imageId(R.drawable.chinese)
                        .build(),
                CropsModel.builder()
                        .cropName("Coconut")
                        .imageId(R.drawable.coconut)
                        .build(),
                CropsModel.builder()
                        .cropName("Coffee")
                        .imageId(R.drawable.coffee)
                        .build(),
                CropsModel.builder()
                        .cropName("Cucumber")
                        .imageId(R.drawable.cucumber)
                        .build(),
                CropsModel.builder()
                        .cropName("Figs")
                        .imageId(R.drawable.figs)
                        .build(),
                CropsModel.builder()
                        .cropName("Ginger")
                        .imageId(R.drawable.ginger)
                        .build(),
                CropsModel.builder()
                        .cropName("Grapes")
                        .imageId(R.drawable.grapes)
                        .build(),
                CropsModel.builder()
                        .cropName("Guava")
                        .imageId(R.drawable.guava)
                        .build(),
                CropsModel.builder()
                        .cropName("Lemon")
                        .imageId(R.drawable.lemon)
                        .build(),
                CropsModel.builder()
                        .cropName("Maize")
                        .imageId(R.drawable.maize)
                        .build(),
                CropsModel.builder()
                        .cropName("Mango")
                        .imageId(R.drawable.mango)
                        .build(),
                CropsModel.builder()
                        .cropName("Onion")
                        .imageId(R.drawable.onion)
                        .build(),
                CropsModel.builder()
                        .cropName("Orange")
                        .imageId(R.drawable.orange)
                        .build(),
                CropsModel.builder()
                        .cropName("Wheat")
                        .imageId(R.drawable.wheat)
                        .build(),
                CropsModel.builder()
                        .cropName("Papaya")
                        .imageId(R.drawable.papaya)
                        .build(),
                CropsModel.builder()
                        .cropName("Peach")
                        .imageId(R.drawable.peach)
                        .build(),
                CropsModel.builder()
                        .cropName("Pear")
                        .imageId(R.drawable.pear)
                        .build(),
                CropsModel.builder()
                        .cropName("Pineapple")
                        .imageId(R.drawable.pineapple)
                        .build(),
                CropsModel.builder()
                        .cropName("Pumpkin")
                        .imageId(R.drawable.pumpkin)
                        .build(),
                CropsModel.builder()
                        .cropName("Strawberry")
                        .imageId(R.drawable.strawberry)
                        .build(),
                CropsModel.builder()
                        .cropName("Sugarcane")
                        .imageId(R.drawable.sugarcane)
                        .build(),
                CropsModel.builder()
                        .cropName("Tangerine")
                        .imageId(R.drawable.tangerine)
                        .build(),
                CropsModel.builder()
                        .cropName("Tobacco")
                        .imageId(R.drawable.tobacco)
                        .build(),
                CropsModel.builder()
                        .cropName("Tomato")
                        .imageId(R.drawable.tomato)
                        .build(),
                CropsModel.builder()
                        .cropName("Millet")
                        .imageId(R.drawable.wheat)
                        .build(),
                CropsModel.builder()
                        .cropName("Other")
                        .imageId(R.drawable.questio)
                        .build()
        ).collect(Collectors.toList());

        AlertDialog alertDialog = searchDialog.create();

        alertDialog.setCanceledOnTouchOutside(false);

        CropsGridAdapter cropsGridAdapter = new CropsGridAdapter(this, cropsModels);
        cropsGrid.setAdapter(cropsGridAdapter);

        edSearchCrop.addTextChangedListener(new TextValidator(edSearchCrop) {
            @Override
            public void validate() {
                if (edSearchCrop.getText().toString().length() == 0) {
                    cropsGridAdapter.searchCrop("");
                } else {
                    String text = edSearchCrop.getText().toString().toLowerCase(Locale.getDefault());
                    cropsGridAdapter.searchCrop(text);
                }
            }
        });

        cropsGrid.setOnItemClickListener((adapterView, view, position, id) -> {
            CropsModel cropModel = cropsModels.get(position);
            crop = cropModel.getCropName();
            try {
                stringData.put("crop", crop);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvDisplaySeletedCrop.setText(crop);
            tvDisplaySeletedCrop.setVisibility(View.VISIBLE);
            btnOpenCropsDialog.setVisibility(View.INVISIBLE);
            btnOpenCropsDialog.setEnabled(false);
            alertDialog.dismiss();
        });
        return alertDialog;
    }
}