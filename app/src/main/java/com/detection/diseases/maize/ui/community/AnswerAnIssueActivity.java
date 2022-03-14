package com.detection.diseases.maize.ui.community;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.community.model.IssueAnswerModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnswerAnIssueActivity extends AppCompatActivity {

    IssueAnswersRecyclerAdapter adapter;
    RecyclerView recyclerView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_an_issue);
        initViews();

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
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build(),IssueAnswerModel.builder()
                        .answerContent("That is great")
                        .answerBy("Auga").build()
        ).collect(Collectors.toList());

        adapter = new IssueAnswersRecyclerAdapter(answels, this);
        LinearLayoutManager r = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(r);
        recyclerView.setAdapter(adapter);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.background_dark));
}
    private void initViews() {
        recyclerView = findViewById(R.id.rv_issue_answers);
        collapsingToolbarLayout = findViewById(R.id.collaspring_tool_ba);
    }
}