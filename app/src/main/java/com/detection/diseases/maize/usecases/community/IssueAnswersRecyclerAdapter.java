package com.detection.diseases.maize.usecases.community;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.detection.diseases.maize.usecases.community.model.IssueAnswerModel;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.SneakyThrows;

/**
 * @author Augustine
 *
 * Adapts issue answers to a recycler view with a custom loaded xml view file
 */
public class IssueAnswersRecyclerAdapter extends RecyclerView.Adapter<IssueAnswersRecyclerAdapter.IssueAnswerHolder> {

    /**
     * A list of answers
     */
    private final List<IssueAnswerModel> issueAnswers;

    /**
     * The context where the answers will be initialised
     */
    private final Context context;

    /**
     *A constructor. Enable building objects with initial list of issuAnswerModel and Context
     *
     * @param issueAnswers List of {@link IssueAnswerModel}
     * @param context The context where the answers will be initialised
     */
    public IssueAnswersRecyclerAdapter(List<IssueAnswerModel> issueAnswers, Context context) {
        this.issueAnswers = issueAnswers;
        this.context = context;
    }

    /**
     * Inflates a view and return the associated view holder. The view stands for a single answer row
     *
     * @param parent The parent view group for the row
     *
     * @param viewType
     *
     * @return {@link IssueAnswerHolder}
     */
    @NonNull
    @Override
    public IssueAnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.issue_answer_row, parent, false);
        return new IssueAnswerHolder(view);
    }

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull IssueAnswerHolder holder, int position) {
        IssueAnswerModel answer = issueAnswers.get(position);
        holder.answeUser.setText(answer.getAnswerBy());
        holder.content.setText(answer.getAnswerContent());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date createdData = sdf.parse(answer.getCreatedAt());
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
            daysToDisplay = createdData.getDay() + "-" + createdData.getMonth() + "-" + createdData.getYear();
        }
        holder.tvCreated.setText(daysToDisplay);
        if(answer.getImageAvatarLink() != null){
            holder.answerImage.setClipToOutline(true);
            Picasso.get().load(answer.getImageAvatarLink()).fit().centerCrop().into(holder.answerImage);
            holder.answerImage.setVisibility(View.VISIBLE);
        }

        if(answer.getAnswerBy().equals("Augustine Simwela")){
            Picasso.get().load(R.drawable.auga_disp).fit().centerCrop().into(holder.creatorAvator);
        }

    }

    /**
     * Keeps track of how many issue answers are there for easy recycling
     *
     * @return Total number of answers for a particular issue
     */
    @Override
    public int getItemCount() {
        return issueAnswers.size();
    }

    /**
     * A view holder for the adapter.
     *
     * @see androidx.recyclerview.widget.RecyclerView.ViewHolder
     * @see androidx.recyclerview.widget.RecyclerView.Adapter
     */
    public static class IssueAnswerHolder extends RecyclerView.ViewHolder {
        TextView answeUser, content, tvCreated;
        ImageView answerImage;
        CircleImageView creatorAvator;
        public IssueAnswerHolder(@NonNull View itemView) {
            super(itemView);
            answeUser = itemView.findViewById(R.id.tv_user_answered);
            content = itemView.findViewById(R.id.tv_content);
            answerImage = itemView.findViewById(R.id.iv_display_answer_image);
            tvCreated = itemView.findViewById(R.id.tv_answer_date);
            creatorAvator = itemView.findViewById(R.id.fg_communuty_issue_creator_avatar);
        }
    }
}
