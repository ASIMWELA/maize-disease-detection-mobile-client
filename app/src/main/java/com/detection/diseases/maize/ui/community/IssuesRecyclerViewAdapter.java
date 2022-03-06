package com.detection.diseases.maize.ui.community;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.community.model.CropsModel;
import com.detection.diseases.maize.ui.community.payload.Issue;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IssuesRecyclerViewAdapter extends RecyclerView.Adapter<IssuesRecyclerViewAdapter.IssuesViewHolder> {
    final Context context;
    final List<Issue> issues;
    final List<Issue> searchedModels;

    public IssuesRecyclerViewAdapter(Context context, List<Issue> issues) {
        this.context = context;
        this.issues = issues;
        searchedModels = new ArrayList<>(issues);
    }

    @NonNull
    @Override
    public IssuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_issue_row, parent, false);
        return new IssuesViewHolder(view);
    }

    @SneakyThrows
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull IssuesViewHolder holder, int position) {
        Issue issue = issues.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
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
        holder.tvIssueCreator.setText(issue.getCreatedBy());
        holder.tvIssueCreatedAt.setText(daysToDisplay);
        holder.tvIssueQuestion.setText(issue.getQuestion());
        holder.tvIssueDesc.setText(issue.getQuestionDescription());
        holder.tvIssueNumberOfAnswers.setText(issue.getIssueAnswers());
        holder.tvIssueLikes.setText(issue.getIssueLikes());
        holder.tvIssueDislikes.setText(issue.getIssueDislikes());
        holder.tvCrop.setText(issue.getCrop());
        if (issue.getImageAvatarUrl() != null) {
            holder.ivIssueImage.setClipToOutline(true);
            Picasso.get().load(issue.getImageAvatarUrl()).fit().centerCrop().into(holder.ivIssueImage);
        } else {
            holder.ivIssueImage.setVisibility(View.GONE);
        }
        if (issue.getIssueStatus().equals(IssueStatus.RESOLVED.name())) {
            holder.cpIssueStatus.setVisibility(View.VISIBLE);
        }
        holder.ivIssueCreator.setImageResource(R.drawable.ic_baseline_person_24);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public static class IssuesViewHolder extends RecyclerView.ViewHolder {
        TextView tvIssueCreator,
                tvIssueLikes,
                tvIssueDislikes,
                tvIssueNumberOfAnswers,
                tvIssueDesc,
                tvIssueQuestion,
                tvIssueCreatedAt, tvCrop;

        ImageView ivIssueImage,
                ivIssueLikes,
                ivIssueDislikes,
                ivIssueCreator;
        Chip cpIssueStatus;

        public IssuesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIssueCreator = itemView.findViewById(R.id.fg_community_issue_creator);
            tvIssueDislikes = itemView.findViewById(R.id.fg_community_tv_issue_dislikes);
            tvIssueLikes = itemView.findViewById(R.id.fg_community_tv_likes);
            tvIssueNumberOfAnswers = itemView.findViewById(R.id.fg_community_issue_answers_number);
            tvIssueDesc = itemView.findViewById(R.id.fg_community_tv_issues_question_desc);
            tvIssueQuestion = itemView.findViewById(R.id.fg_community_tv_issue_question);
            tvIssueCreatedAt = itemView.findViewById(R.id.fg_community_issue_creation_date);
            ivIssueImage = itemView.findViewById(R.id.fg_community_issue_img);
            ivIssueLikes = itemView.findViewById(R.id.fg_community_btn_like);
            ivIssueDislikes = itemView.findViewById(R.id.fg_community_issue_dislike);
            ivIssueCreator = itemView.findViewById(R.id.fg_communuty_issue_creator_avatar);
            cpIssueStatus = itemView.findViewById(R.id.fg_community_cp_resolved);
            tvCrop = itemView.findViewById(R.id.tv_community_crop);
        }
    }

    public void searchIssue(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        issues.clear();
        if (charText.length() == 0) {
            issues.addAll(searchedModels);
        } else {
            for (Issue issue : searchedModels) {
                if (issue.getCrop().toLowerCase(Locale.getDefault())
                        .contains(charText) || issue.getCreatedBy().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    issues.add(issue);
                }
            }
        }
        notifyDataSetChanged();
    }

}
