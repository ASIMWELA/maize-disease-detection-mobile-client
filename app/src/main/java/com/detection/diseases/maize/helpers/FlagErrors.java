package com.detection.diseases.maize.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;

/**
 * @author Augustine
 * </p>
 * A Utility class that help in flagging errors
 */
public class FlagErrors {

    /**
     * Defines the context where the errors is flagged
     */
    private final Context context;
    /**
     * Defines the activity where the errors is flagged
     */
    private final Activity activity;

    /**
     * A Creates an object of this class
     * @param context The context where the errors are flagged from
     * @param activity The activity where the errors are flagged from
     */
    public FlagErrors(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * A Utility function that creates an Popup Dialog
     *
     * @param apiError Error object of type {@link VolleyError}
     */
    @SuppressLint({"InflateParams", "SetTextI18n"})
    @SneakyThrows
    public void flagApiError(VolleyError apiError){
        AlertDialog.Builder searchDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.api_app_dialog_error, null);
        searchDialog.setView(dialogView);
        TextView tvGeneralError = dialogView.findViewById(R.id.error_dialog_error_content);
        Button btnDismisDialog = dialogView.findViewById(R.id.error_dialog_dismiss_dialog);
        AlertDialog r = searchDialog.create();
        r.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        r.setCanceledOnTouchOutside(false);
        if(apiError instanceof NoConnectionError) {
            tvGeneralError.setText("There is No Internet Connection Available.");
            r.show();
        }
        else {
            int errorCode = apiError.networkResponse.statusCode;
            String body;
            body = new String(apiError.networkResponse.data, StandardCharsets.UTF_8);
            JSONObject errorOb = new JSONObject(body);
            String message = errorOb.getString("message");
            switch (errorCode){
                case 400:
                    tvGeneralError.setText("Bad Request. Server Could not\nprocess This Request.");
                    r.show();
                    break;
                case 401:
                    tvGeneralError.setText("Server could not authenticate your identity. Re-supply your credentials.");
                    r.show();
                    break;
                case 403:
                    tvGeneralError.setText("Your are not allowed to\ndo this operation.");
                    r.show();
                    break;
                case 422:
                    tvGeneralError.setText("The server could not process\n your entity.");
                    r.show();
                    break;
                case 409:
                    tvGeneralError.setText(message);
                    r.show();
                    break;
                case 404:
                    tvGeneralError.setText(message);
                    r.show();
                    break;
                case 500:
                    tvGeneralError.setText("The sever could not\nprocess your request.");
                    r.show();
                    break;
                default:
                    tvGeneralError.setText("Server could not process\nyour request.");
                    r.show();
            }

        }
        btnDismisDialog.setOnClickListener(v->{
            r.dismiss();
        });
    }

    public void flagValidationError(int baseViewId){
        Snackbar s = Snackbar.make(activity.findViewById(baseViewId), "There are errors in your inputs",
                Snackbar.LENGTH_SHORT)
                .setBackgroundTint(activity.getResources().getColor(R.color.white))
                .setTextColor(activity.getResources().getColor(R.color.teal_200));
        View v = s.getView();
        TextView tv = v.findViewById(com.google.android.material.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        s.show();
    }
}