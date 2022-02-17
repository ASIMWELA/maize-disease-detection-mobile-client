package com.detection.diseases.maize.ui.signup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.ChangeEditTextDrawables;
import com.detection.diseases.maize.helpers.TextValidator;
import com.google.android.material.chip.Chip;
import com.kusu.loadingbutton.LoadingButton;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class SignUpActivity extends AppCompatActivity implements SignupContract.View {
    private EditText edFirstName, edLastName, edEmail, edPassword;
    private Chip backButton;
    private LoadingButton btnSendRequest;
    private String email, firstName, lastName, password;
    private SignUpPresenter signUpPresenter;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        signUpPresenter = new SignUpPresenter(this, this);
        signUpPresenter.initiateInputValidation();
        backButton.setOnClickListener(v->onBackPressed());
        data = new JSONObject();
        btnSendRequest.setOnClickListener(v->signUpPresenter.sendSendSignupRequest(data));
    }

    private void initViews() {
        edEmail = findViewById(R.id.sign_up_tv_email);
        edFirstName = findViewById(R.id.sign_up_tv_first_name);
        edLastName = findViewById(R.id.sign_up_tv_last_name);
        edPassword = findViewById(R.id.sign_up_tv_password);
        backButton = findViewById(R.id.sign_up_cp_back);
        btnSendRequest = findViewById(R.id.sign_up_activity_btn_submit);
    }

    @Override
    public boolean validateInput() {
        edLastName.addTextChangedListener(new TextValidator(edLastName) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            @SneakyThrows
            public void validate() {
                if (!edLastName.getText().toString().isEmpty()) {
                    if (edLastName.getText().toString().trim().length() < 3) {
                        ChangeEditTextDrawables.changeToErrorDrawable(edLastName, R.drawable.ic_baseline_person_24, getApplicationContext());
                        edLastName.setError("last name too short");
                        lastName = null;
                    } else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edLastName, R.drawable.ic_baseline_person_24, getApplicationContext());
                        lastName = edLastName.getText().toString().trim();
                        data.put("lastName", lastName);
                    }
                }else {
                    ChangeEditTextDrawables.changeToNormalDrawable(edLastName, R.drawable.ic_baseline_person_24, getApplicationContext());
                    edLastName.setError(null);
                }
            }
        });

        edFirstName.addTextChangedListener(new TextValidator(edFirstName) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            @SneakyThrows
            public void validate() {
                if (!edFirstName.getText().toString().isEmpty()) {
                    if (edFirstName.getText().toString().trim().length() < 3) {
                        ChangeEditTextDrawables.changeToErrorDrawable(edFirstName, R.drawable.ic_baseline_person_first_name_24, getApplicationContext());
                        edFirstName.setError("first name too short");
                        firstName = null;
                    } else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edFirstName, R.drawable.ic_baseline_person_first_name_24, getApplicationContext());
                        firstName = edFirstName.getText().toString().trim();
                        data.put("firstName", firstName);
                    }
                }else {
                    ChangeEditTextDrawables.changeToNormalDrawable(edFirstName, R.drawable.ic_baseline_person_first_name_24, getApplicationContext());
                    edFirstName.setError(null);
                }

            }
        });


        edEmail.addTextChangedListener(new TextValidator(edEmail) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            @SneakyThrows
            public void validate() {
                if (!edEmail.getText().toString().isEmpty()) {
                    String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if(!edEmail.getText().toString().matches(emailRegex)){
                        ChangeEditTextDrawables.changeToErrorDrawable(edEmail, R.drawable.ic_baseline_email_24, getApplicationContext());
                        edEmail.setError("Invalid email");
                        email = null;
                    }else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edEmail, R.drawable.ic_baseline_email_24, getApplicationContext());
                        email = edEmail.getText().toString().trim();
                        data.put("email", email);
                    }
                }else {
                    ChangeEditTextDrawables.changeToNormalDrawable(edEmail, R.drawable.ic_baseline_email_24, getApplicationContext());
                    edEmail.setError(null);
                }

            }
        });

        edPassword.addTextChangedListener(new TextValidator(edPassword) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            @SneakyThrows
            public void validate() {
                if (!edPassword.getText().toString().isEmpty()) {
                    if (edPassword.getText().toString().trim().length() < 6) {
                        ChangeEditTextDrawables.changeToErrorDrawable(edPassword, R.drawable.ic_baseline_lock_24, getApplicationContext());
                        edPassword.setError("password too short");
                        password = null;
                    } else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edPassword, R.drawable.ic_baseline_lock_24, getApplicationContext());
                        password = edPassword.getText().toString().trim();
                        data.put("password", password);
                    }
                }else{
                    ChangeEditTextDrawables.changeToNormalDrawable(edPassword, R.drawable.ic_baseline_lock_24, getApplicationContext());
                    edPassword.setError(null);
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
        Toast.makeText(this, "Validation errors", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingProgress() {
        btnSendRequest.setEnabled(false);
        btnSendRequest.showLoading();
    }

    @Override
    public void hideLoadingProgress() {
        btnSendRequest.setEnabled(true);
        btnSendRequest.hideLoading();
    }

    @Override
    public void onSignupSucess(JSONObject response) {
        Toast.makeText(this, "Sucess: "+response.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSignupError(VolleyError error) {
        Toast.makeText(this, "Error:" + error.toString(), Toast.LENGTH_SHORT).show();

    }
}