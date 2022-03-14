package com.detection.diseases.maize.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.community.model.IssueAnswerModel;

import java.util.List;

public class IssueAnswersRecyclerAdapter extends RecyclerView.Adapter<IssueAnswersRecyclerAdapter.IssueAnswerHolder> {

    private final List<IssueAnswerModel> issueAnswers;
    private final Context context;

    public IssueAnswersRecyclerAdapter(List<IssueAnswerModel> issueAnswers, Context context) {
        this.issueAnswers = issueAnswers;
        this.context = context;
    }

    @NonNull
    @Override
    public IssueAnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.issue_answer_row, parent, false);
        return new IssueAnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueAnswerHolder holder, int position) {
        IssueAnswerModel answer = issueAnswers.get(position);
        holder.title.setText(answer.getAnswerBy());
        holder.content.setText(answer.getAnswerContent());
    }

    @Override
    public int getItemCount() {
        return issueAnswers.size();
    }

    public static class IssueAnswerHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        public IssueAnswerHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
        }
    }
}
