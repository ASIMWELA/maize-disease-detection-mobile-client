package com.detection.diseases.maize.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.commons.TextValidator;
import com.google.android.material.chip.Chip;

import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements SignupContract.View {
    private EditText edFirstName, edLastName, edEmail, edPassword;
    private ProgressBar pgShowProgress;
    private Chip backButton;
    private AppCompatButton btnSendRequest;
    private String email, firstName, lastName, password;
    private SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        signUpPresenter = new SignUpPresenter(this, this);
        signUpPresenter.initiateInputValidation();


    }

    private void initViews() {
        edEmail = findViewById(R.id.sign_up_tv_email);
        edFirstName = findViewById(R.id.sign_up_tv_first_name);
        edLastName = findViewById(R.id.sign_up_tv_last_name);
        edPassword = findViewById(R.id.sign_up_tv_password);
        pgShowProgress = findViewById(R.id.sign_up_progress_bar);
        backButton = findViewById(R.id.sign_up_cp_back);
        btnSendRequest = findViewById(R.id.sign_up_activity_btn_submit);
    }

    @Override
    public boolean validateInput() {
        edLastName.addTextChangedListener(new TextValidator(edLastName) {
            @Override
            public void validate() {
                if (!edLastName.getText().toString().isEmpty()) {
                    if (edLastName.getText().toString().trim().length() < 2) {
                        edLastName.setBackgroundResource(R.drawable.rounded_boaders_error);
                        edLastName.setError("last name too short");
                        lastName = null;
                    } else {
                        edLastName.setBackgroundResource(R.drawable.input_rounded_corners);
                        lastName = edLastName.getText().toString().trim();
                    }
                }
            }
        });

        edFirstName.addTextChangedListener(new TextValidator(edFirstName) {
            @Override
            public void validate() {
                if (!edFirstName.getText().toString().isEmpty()) {
                    if (edFirstName.getText().toString().trim().length() < 2) {
                        edFirstName.setBackgroundResource(R.drawable.rounded_boaders_error);
                        edFirstName.setError("first name too short");
                        firstName = null;
                    } else {
                        edFirstName.setBackgroundResource(R.drawable.input_rounded_corners);
                        firstName = edFirstName.getText().toString().trim();
                    }
                }
            }
        });


        edEmail.addTextChangedListener(new TextValidator(edEmail) {
            @Override
            public void validate() {
                if (!edEmail.getText().toString().isEmpty()) {
                    String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if(!edEmail.getText().toString().matches(emailRegex)){
                        edEmail.setError("Invalid email");
                        edEmail.setBackgroundResource(R.drawable.rounded_boaders_error);
                        email = null;
                    }else {
                        edEmail.setBackgroundResource(R.drawable.input_rounded_corners);
                        email = edEmail.getText().toString().trim();
                    }
                }
            }
        });

        edPassword.addTextChangedListener(new TextValidator(edPassword) {
            @Override
            public void validate() {
                if (!edPassword.getText().toString().isEmpty()) {
                    if (edPassword.getText().toString().trim().length() < 6) {
                        edPassword.setBackgroundResource(R.drawable.rounded_boaders_error);
                        edPassword.setError("password too short");
                        password = null;
                    } else {
                        edPassword.setBackgroundResource(R.drawable.input_rounded_corners);
                        password = edPassword.getText().toString().trim();
                    }
                }

            }
        });
        return email != null
                && firstName != null
                && lastName != null
                && password != null;
    }

    @Override
    public void onValidationError() {

    }

    @Override
    public void showLoadingProgress() {
        btnSendRequest.setClickable(false);
        pgShowProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingProgress() {
        btnSendRequest.setClickable(true);
        pgShowProgress.setVisibility(View.GONE);
    }

    @Override
    public void onSignupSucess(JSONObject response) {

    }

    @Override
    public void onSignupError(VolleyError error) {

    }
}