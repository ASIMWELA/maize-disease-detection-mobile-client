package com.detection.diseases.maize.usecases.community;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.CheckUserSession;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.helpers.VolleyController;
import com.detection.diseases.maize.usecases.community.payload.Issue;
import com.detection.diseases.maize.usecases.signin.LoggedInUserModel;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

/**
 * @author Augustine
 *
 * Adapts issues to a recycler view with a custom loaded xml view file
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IssuesRecyclerViewAdapter extends RecyclerView.Adapter<IssuesRecyclerViewAdapter.IssuesViewHolder> {
    /**
     * Context where the adapter is created
     */
    final Context context;

    /**
     * The issue list that are populated into the view
     */
    final List<Issue> issues;

    /**
     * Issues that are upated when they meet the search criteria
     */
    final List<Issue> searchedModels;

    /**
     * Allows getting a user from session
     */
    final Gson gson = new Gson();

    /**
     * Keeps track of user session
     */
    final SessionManager sessionManager;

    /**
     * The actual logged in user model
     */
    LoggedInUserModel user = null;

    /**
     *A constructor. Enable building objects with initial list of issuAnswerModel and Context
     *
     * @param issues List of {@link Issue}
     * @param context The context where the answers will be initialised
     */
    public IssuesRecyclerViewAdapter(Context context, List<Issue> issues) {
        this.context = context;
        this.issues = issues;
        searchedModels = new ArrayList<>(issues);
        sessionManager = new SessionManager(context);
    }

    /**
     * Inflates a view and return the associated view holder. The view stands for a single answer row
     *
     * @param parent The parent view group for the row
     *
     * @param viewType
     *
     * @return {@link com.detection.diseases.maize.usecases.community.IssuesRecyclerViewAdapter.IssuesViewHolder}
     */

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
        if (days > 30) {

            Calendar c = Calendar.getInstance();
            c.setTime(createdData);
            int year = c.get(Calendar.YEAR);
            String month = c.get(Calendar.MONTH) < 10 ? "0" + c.get(Calendar.MONTH) : c.get(Calendar.MONTH) + "";
            String day = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH) + "";
            daysToDisplay = day + "-" + month + "-" + year;
        }

        int issueAnswers = Integer.parseInt(issue.getIssueAnswers());

        holder.tvIssueCreator.setText(issue.getCreatedBy());
        holder.tvIssueCreatedAt.setText(daysToDisplay);
        holder.tvIssueQuestion.setText(issue.getQuestion());
        holder.tvIssueDesc.setText(issue.getQuestionDescription());
        holder.tvIssueNumberOfAnswers.setText(issueAnswers == 1 ? issueAnswers + " answer" : issueAnswers + " answers");
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
            holder.tvResolvedStatus.setVisibility(View.VISIBLE);
        }
        holder.ivIssueCreator.setImageResource(R.drawable.ic_baseline_person_24);
        holder.baseView.setOnClickListener(v -> {
            Intent i = new Intent(context, AnswerAnIssueActivity.class);
            i.putExtra(AppConstants.ISSUE_TO_ANSWER, issue);
            context.startActivity(i);
        });

        if (CheckUserSession.isUserLoggedIn(context)) {
            user = gson.fromJson(sessionManager.getLoggedInUser(), LoggedInUserModel.class);
            if ((user.getFirstName() + " " + user.getLastName()).equals(issue.getCreatedBy())
                    && !(issue.getIssueStatus().equals(IssueStatus.RESOLVED.name()))
                    && issueAnswers > 0) {
                holder.cpMarkAsResolved.setVisibility(View.VISIBLE);
            }
        }

        if (issue.getIssueStatus().equals(IssueStatus.RESOLVED.name())) {
            holder.tvResolvedStatus.setVisibility(View.VISIBLE);
            holder.cpMarkAsResolved.setVisibility(View.GONE);
        }
        if (issue.getCreatedBy().equals("Augustine Simwela")) {
            Picasso.get().load(R.drawable.auga_disp).fit().centerCrop().into(holder.ivIssueCreator);
        }
        holder.cpMarkAsResolved.setOnClickListener(v -> {
            holder.pgShoResolvePb.setVisibility(View.VISIBLE);
            holder.cpMarkAsResolved.setVisibility(View.GONE);
            JsonObjectRequest markResolved = new JsonObjectRequest(
                    Request.Method.PUT,
                    AppConstants.BASE_API_URL + "/community/issues/resolve-issue/" + issue.getUuid() + "/" + user.getUuid(),
                    null,
                    response -> {
                        holder.pgShoResolvePb.setVisibility(View.GONE);
                        holder.cpMarkAsResolved.setVisibility(View.GONE);
                        holder.tvResolvedStatus.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Issue marked as resolved", Toast.LENGTH_SHORT).show();
                    }, error -> {
                holder.pgShoResolvePb.setVisibility(View.GONE);
                holder.cpMarkAsResolved.setVisibility(View.VISIBLE);
                holder.tvResolvedStatus.setVisibility(View.GONE);
                Toast.makeText(context, "We were unable to complete the action", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + sessionManager.getToken());
                    return headers;
                }
            };

            markResolved.setTag("send_resolve_issue");
            VolleyController.getInstance(context).getRequestQueue().add(markResolved);

        });

    }
    /**
     * Keeps track of how many issues are there for easy recycling
     *
     * @return Total number of answers for a particular issue
     */
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
                tvIssueCreatedAt, tvCrop, tvResolvedStatus;

        ImageView ivIssueImage,
                ivIssueLikes,
                ivIssueDislikes,
                ivIssueCreator;
        Chip cpMarkAsResolved;
        CardView baseView;
        ProgressBar pgShoResolvePb;
        CircleImageView createorAvator;

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
            tvCrop = itemView.findViewById(R.id.tv_community_crop);
            baseView = itemView.findViewById(R.id.fg_community_issue_row);
            cpMarkAsResolved = itemView.findViewById(R.id.cp_mark_as_resolved);
            pgShoResolvePb = itemView.findViewById(R.id.pb_resolve_pb);
            tvResolvedStatus = itemView.findViewById(R.id.tv_resolved_status);

        }
    }

    /**
     * Seachh issues based on the input text.
     *
     * @param charText Text to look for in the crops or the issue creator
     */
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
