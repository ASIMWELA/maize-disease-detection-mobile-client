package com.detection.diseases.maize.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.community.payload.Issue;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IssuesRecyclerViewAdapter extends RecyclerView.Adapter<IssuesRecyclerViewAdapter.IssuesViewHolder> {
    Context context;
    List<Issue> issues;
    @NonNull
    @Override
    public IssuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_issue_row, parent, false);
        return new IssuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssuesViewHolder holder, int position) {
        Issue issue = issues.get(position);
        holder.tvIssueCreator.setText(issue.getCreatedBy());
        holder.tvIssueCreatedAt.setText(issue.getCreatedAt());
        holder.tvIssueQuestion.setText(issue.getQuestion());
        holder.tvIssueDesc.setText(issue.getQuestionDescription());
        holder.tvIssueNumberOfAnswers.setText(issue.getIssueAnswers());
        holder.tvIssueLikes.setText(issue.getIssueLikes());
        holder.tvIssueDislikes.setText(issue.getIssueDislikes());
        Picasso.get().load(issue.getImageAvatarUrl()).into(holder.ivIssueImage);
        if(issue.getIssueStatus().equals(IssueStatus.RESOLVED.name())){
            holder.cpIssueStatus.setVisibility(View.VISIBLE);
        }
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
                tvIssueCreatedAt;

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
        }
    }
}
