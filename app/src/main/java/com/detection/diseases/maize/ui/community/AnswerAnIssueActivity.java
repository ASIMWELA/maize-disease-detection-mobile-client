package com.detection.diseases.maize.ui.community;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.ui.community.model.IssueAnswerModel;
import com.detection.diseases.maize.ui.community.payload.Issue;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.SneakyThrows;

public class AnswerAnIssueActivity extends AppCompatActivity {

    private IssueAnswersRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView ivIssueImage;
    private TextView tvTitle, tvQuestion, tvCreatorName, tvDisplayDate;

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
        Issue issue = getIntent().getParcelableExtra(AppConstants.ISSUE_TO_ANSWER);

        String formatedTitle = issue.getQuestion().substring(0, 1).toUpperCase() + issue.getQuestion().substring(1);
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
        if(days>30){
            daysToDisplay =createdData.getDay()+ "-"+ createdData.getMonth()+"-"+ createdData.getYear();
        }
        tvDisplayDate.setText(daysToDisplay);
        Picasso.get().load(issue.getImageAvatarUrl()).fit().centerCrop().into(ivIssueImage);
        List<IssueAnswerModel> answels = Stream.of(
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),
                IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(), IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build()
        ).collect(Collectors.toList());

        adapter = new IssueAnswersRecyclerAdapter(answels, this);
        LinearLayoutManager r = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(r);
        recyclerView.setAdapter(adapter);
   }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_issue_answers);
        collapsingToolbarLayout = findViewById(R.id.collaspring_tool_ba);
        toolbar = findViewById(R.id.answer_issue_tool_bar);
        ivIssueImage = findViewById(R.id.issue_to_answer_image_holder);
        tvTitle = findViewById(R.id.tv_question_answer_issue);
        tvQuestion = findViewById(R.id.tv_show_question_desc);
        tvCreatorName = findViewById(R.id.tv_creator_name);
        tvDisplayDate = findViewById(R.id.tv_display_date);

    }
}