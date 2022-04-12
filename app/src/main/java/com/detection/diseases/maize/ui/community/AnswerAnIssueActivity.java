package com.detection.diseases.maize.ui.community;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.CheckUserSession;
import com.detection.diseases.maize.helpers.RealPathUtil;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.helpers.TextValidator;
import com.detection.diseases.maize.ui.community.model.IssueAnswerModel;
import com.detection.diseases.maize.ui.community.payload.Issue;
import com.detection.diseases.maize.ui.signin.LoggedInUserModel;
import com.detection.diseases.maize.ui.signin.SigninActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.SneakyThrows;

import static com.detection.diseases.maize.ui.camera.CameraActivity.REQUEST_ID_MULTIPLE_PERMISSIONS;

/**
 * @author Augustine
 *
 * An activity responsible for creating managing issue answers
 */
public class AnswerAnIssueActivity extends AppCompatActivity implements AnswerIssueContract.View {

    /**
     * A Waiting code for image selection
     */
    private static final int SELECT_IMAGE_CODE = 100;

    /**
     * A recycler Adapter that adapts {@link IssueAnswerModel} to an adapter
     */
    private IssueAnswersRecyclerAdapter adapter;

    /**
     * The base view of the recyclerView
     */
    private RecyclerView recyclerView;

    /**
     * Collapsing toolbar for behaviour of a of hidding issue image and loading the bar
     */
    private CollapsingToolbarLayout collapsingToolbarLayout;


    private Toolbar toolbar;
    private ImageView ivIssueImage, ivDismissSelectedImage, ivDisplaySelectedImage, ivSendIssueAnswer, ivOpenGallley;
    private TextView tvTitle, tvQuestion, tvNoIssueAnswers, tvCreatorName, tvDisplayDate;
    private EditText edInputIssueAnswer;
    private ProgressBar pbViewAnswerIssueProgress, pbGetIssueAnswersProgressBar;
    private String selectedImageUri, answerContent;
    private List<IssueAnswerModel> issueAnswerModelList = new ArrayList<>();

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    @SneakyThrows
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_an_issue);
        initViews();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        Gson gson = new Gson();
        Issue issue = getIntent().getParcelableExtra(AppConstants.ISSUE_TO_ANSWER);
        SessionManager sessionManager = new SessionManager(this);
        AnswerIssuePresenter answerIssuePresenter = new AnswerIssuePresenter(this, this);

        //initialise validation
        answerIssuePresenter.initValidation();
        answerIssuePresenter.getIssueAnswers(issue.getUuid());

        ivSendIssueAnswer.setOnClickListener(v -> {
            if (!CheckUserSession.isUserLoggedIn(this)) {
                Intent i = new Intent(this, SigninActivity.class);
                startActivity(i);
                Toast.makeText(this, "Login to take an action", Toast.LENGTH_SHORT).show();
            } else {
                LoggedInUserModel loggedInUserModel = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
                String token = sessionManager.getToken();
                RequestParams data = new RequestParams();
                JSONObject answer = new JSONObject();
                if (selectedImageUri != null) {
                    try {
                        answer.put("answer", answerContent);
                        File image = new File(selectedImageUri);
                        data.put("answer", answer.toString());
                        data.put("image", image);
                        answerIssuePresenter.sendAnswer(data, issue.getUuid(), loggedInUserModel.getUuid(), token);
                    } catch (JSONException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        answer.put("answer", answerContent);
                        data.put("answer", answer.toString());
                        answerIssuePresenter.sendAnswer(data, issue.getUuid(), loggedInUserModel.getUuid(), token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        ivDismissSelectedImage.setOnClickListener(v -> {
            selectedImageUri = null;
            ivDisplaySelectedImage.setVisibility(View.GONE);
            ivDisplaySelectedImage.setImageDrawable(null);
            ivDismissSelectedImage.setVisibility(View.GONE);

        });
        //capitalize the first char
        String formatedTitle = issue.getQuestion()
                .substring(0, 1).toUpperCase()
                + issue.getQuestion().substring(1);


        collapsingToolbarLayout.setTitle(formatedTitle);
        tvTitle.setText(formatedTitle);
        tvQuestion.setText(issue.getQuestionDescription());
        tvCreatorName.setText(issue.getCreatedBy());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date createdData = sdf.parse(issue.getCreatedAt());
        LocalDate createdAt = createdData.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        long days = ChronoUnit.DAYS.between(createdAt, now);
        String daysToDisplay = null;
        if (days == 0) {
            daysToDisplay = "Today";
        }
        if (days == 1) {
            daysToDisplay = "Yesterday";
        }
        if (days > 1) {
            daysToDisplay = days + " days ago";
        }
        if (days > 30) {
            Calendar c = Calendar.getInstance();
            c.setTime(createdData);
            int year = c.get(Calendar.YEAR);
            String month = c.get(Calendar.MONTH) < 10? "0"+c.get(Calendar.MONTH):c.get(Calendar.MONTH)+"";
            String day  = c.get(Calendar.DAY_OF_MONTH) < 10? "0"+c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH)+"" ;
            daysToDisplay = day + "-" + month + "-" + year;     }
        tvDisplayDate.setText(daysToDisplay);
        Picasso.get().load(issue.getImageAvatarUrl()).fit().centerCrop().into(ivIssueImage);

        ivOpenGallley.setOnClickListener(v -> {
            if (checkAndRequestPermissions()) {
                loadGalley();
            }
        });
    }


    /**
     * Initialise all the views of the this activity
     */
    private void initViews() {
        recyclerView = findViewById(R.id.rv_issue_answers);
        collapsingToolbarLayout = findViewById(R.id.collaspring_tool_ba);
        toolbar = findViewById(R.id.answer_issue_tool_bar);
        ivIssueImage = findViewById(R.id.issue_to_answer_image_holder);
        tvTitle = findViewById(R.id.tv_question_answer_issue);
        tvQuestion = findViewById(R.id.tv_show_question_desc);
        tvCreatorName = findViewById(R.id.tv_creator_name);
        tvDisplayDate = findViewById(R.id.tv_display_date);
        edInputIssueAnswer = findViewById(R.id.ed_answe_issue_issue_answer);
        ivOpenGallley = findViewById(R.id.iv_open_select_image);
        ivDisplaySelectedImage = findViewById(R.id.iv_display_selected_image);
        ivSendIssueAnswer = findViewById(R.id.iv_send_issue_answer);
        ivDismissSelectedImage = findViewById(R.id.iv_dismiss_selected_image);
        pbViewAnswerIssueProgress = findViewById(R.id.pb_show_loading);
        pbGetIssueAnswersProgressBar = findViewById(R.id.pb_get_issue_answers_progress_bar);
        tvNoIssueAnswers = findViewById(R.id.tvNoAswers);


        //init adapter
        adapter = new IssueAnswersRecyclerAdapter(issueAnswerModelList, this);
        LinearLayoutManager r = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(r);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Invoked upon successfully answering an issue
     *
     * @param response A JSONObject of the answered issue
     */
    @Override
    @SneakyThrows
    public void onAnswerSuccess(JSONObject response) {
        edInputIssueAnswer.setText("");
        ivDismissSelectedImage.setVisibility(View.GONE);
        ivDisplaySelectedImage.setVisibility(View.GONE);

       //get the recent added
        JSONObject issueObject = response.getJSONObject("_embedded")
                .getJSONArray("answers")
                .getJSONObject(0);
        String imageUrl = null;

        if (!issueObject.isNull("_links")) {
            if (!issueObject.getJSONObject("_links").isNull("answerImage")) {
                imageUrl = issueObject.getJSONObject("_links").getJSONObject("answerImage").getString("href");
            }
        }
        IssueAnswerModel answer = IssueAnswerModel.builder()
                .uuid(issueObject.getString("uuid"))
                .answerBy(issueObject.getString("answerBy"))
                .answerContent(issueObject.getString("answerContent"))
                .createdAt(issueObject.getString("createdAt"))
                .imageAvatarLink(imageUrl)
                .build();

        issueAnswerModelList.add(0, answer);
        adapter.notifyDataSetChanged();
    }

    /**
     * Invoked when there is an API error when sending an answer to an issue
     *
     *
     * @param error A stringified error object
     */
    @Override
    public void onAnswerError(String error) {
        Toast.makeText(this, "Error" + error, Toast.LENGTH_SHORT).show();


    }

    /**
     * Show progress of answer issue Request
     */
    @Override
    public void showLoading() {
        ivSendIssueAnswer.setVisibility(View.GONE);
        pbViewAnswerIssueProgress.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progress of answer issue Request
     */
    @Override
    public void hideLoading() {
        pbViewAnswerIssueProgress.setVisibility(View.GONE);
        ivSendIssueAnswer.setVisibility(View.VISIBLE);
    }

    /**
     * Validate user input
     *
     * @return true if all the inputs are valid otherwise false
     */
    @Override
    public boolean validateInput() {
        edInputIssueAnswer.addTextChangedListener(new TextValidator(edInputIssueAnswer) {
            @Override
            public void validate() {
                if (edInputIssueAnswer.getText().toString().trim().isEmpty()) {
                    answerContent = null;
                } else {
                    answerContent = edInputIssueAnswer.getText().toString().trim();
                }
            }
        });
        return answerContent != null;
    }

    /**
     * Invoked when user inputs are invalid
     */
    @Override
    public void onInputValidationFailed() {
        Toast.makeText(this, "Please provide and answer", Toast.LENGTH_SHORT).show();
    }

    /**
     * Loads gallery to pick an image for issue
     */
    @Override
    public void loadGalley() {
        Intent selectImage = new Intent();
        selectImage.setType("image/*");
        selectImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(selectImage, SELECT_IMAGE_CODE);
    }


    /**
     * Show loading for getting issue answers
     */
    @Override
    public void showGetIssueAnswerLoading() {
        pbGetIssueAnswersProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide loading for getting issue answers
     *
     * Invoked when there is an API error or the issue answers are retrieved
     *
     * @see AnswerIssuePresenter
     */
    @Override
    public void hideGetIssueAnswerLoader() {
        pbGetIssueAnswersProgressBar.setVisibility(View.GONE);
    }

    /**
     * Invoked when get issues flags error
     *
     * @param error A VolleyError object that contains error details
     */
    @Override
    public void onGetIssueAnswersError(VolleyError error) {
        Toast.makeText(this, "Error! Unable to get answers", Toast.LENGTH_SHORT).show();
    }

    /**
     * Invoked when getting issue answers is successful
     *
     * @param response A JSONObect that contains a list of one issue answers
     */
    @Override
    @SneakyThrows
    public void onGetIssueAnswerResponse(JSONObject response) {
        JSONArray answersArray = response.getJSONObject("_embedded").getJSONArray("answers");
        if (answersArray.length() == 0) {
            tvNoIssueAnswers.setVisibility(View.VISIBLE);
        } else {
            for (int x = 0; x < answersArray.length(); x++) {
                JSONObject issueObject = response.getJSONObject("_embedded")
                        .getJSONArray("answers")
                        .getJSONObject(x);
                String imageUrl = null;
                if (!issueObject.isNull("_links")) {
                    if (!issueObject.getJSONObject("_links").isNull("answerImage")) {
                        imageUrl = issueObject.getJSONObject("_links").getJSONObject("answerImage").getString("href");

                    }
                }
                IssueAnswerModel answer = IssueAnswerModel.builder()
                        .uuid(issueObject.getString("uuid"))
                        .answerBy(issueObject.getString("answerBy"))
                        .answerContent(issueObject.getString("answerContent"))
                        .createdAt(issueObject.getString("createdAt"))
                        .imageAvatarLink(imageUrl)
                        .build();
                issueAnswerModelList.add(answer);
            }

            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Request peremission to read external storage
     *
     * @return true if permission is granted false otherwise
     */
    public boolean checkAndRequestPermissions() {
        int extstorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            Uri galleyUri = data.getData();
            Picasso.get().load(galleyUri).fit().into(ivDisplaySelectedImage);
            ivDisplaySelectedImage.setVisibility(View.VISIBLE);
            selectedImageUri = RealPathUtil.getRealPathFromURI_API19(this, galleyUri);
            ivDismissSelectedImage.setVisibility(View.VISIBLE);

        }

    }

}