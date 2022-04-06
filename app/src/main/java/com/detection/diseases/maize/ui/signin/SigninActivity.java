package com.detection.diseases.maize.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.ChangeEditTextDrawables;
import com.detection.diseases.maize.helpers.EUserRoles;
import com.detection.diseases.maize.helpers.FlagErrors;
import com.detection.diseases.maize.helpers.SessionManager;
import com.detection.diseases.maize.helpers.TextValidator;
import com.detection.diseases.maize.ui.signup.SignUpActivity;
import com.google.android.material.chip.Chip;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class SigninActivity extends AppCompatActivity implements SigninContract.View {
    private SigninPresenter signinPresenter;
    private EditText edPassword, edEmail;
    private Chip cpBbacArrow;
    private TextView tvOpenSignUp;
    private Button btnSendLoginRequest;
    private String email, password;
    private FlagErrors flagErrors;
    private SessionManager sessionManager;
    JSONObject data = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();
        signinPresenter = new SigninPresenter(this, this);
        sessionManager = new SessionManager(this);
        flagErrors = new FlagErrors(this, this);

        cpBbacArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        tvOpenSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        signinPresenter.initInputValidation();
        btnSendLoginRequest.setOnClickListener(v -> signinPresenter.sendSignRequest(data));
    }

    private void initViews() {
        edEmail = findViewById(R.id.sign_in_activity_email);
        edPassword = findViewById(R.id.sign_in_tv_password);
        cpBbacArrow = findViewById(R.id.sign_in_cp_back);
        tvOpenSignUp = findViewById(R.id.sign_in_tv_open_sign_up);
        btnSendLoginRequest = findViewById(R.id.sign_in_activity_btn_submit);
    }


    @Override
    public boolean validateInput() {
        edPassword.addTextChangedListener(new TextValidator(edPassword) {
            @Override
            @SneakyThrows
            public void validate() {
                if (!edPassword.getText().toString().isEmpty()) {
                    if (edPassword.getText().toString().trim().length() < 5) {
                        ChangeEditTextDrawables.changeToErrorDrawable(edPassword, R.drawable.ic_baseline_lock_24, SigninActivity.this);
                        edPassword.setError("Valid password required");
                        password = null;
                    } else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edPassword, R.drawable.ic_baseline_lock_24, SigninActivity.this);
                        password = edPassword.getText().toString().trim();
                        data.put("password", password);
                    }
                } else {
                    ChangeEditTextDrawables.changeToNormalDrawable(edPassword, R.drawable.ic_baseline_lock_24, SigninActivity.this);
                    edPassword.setError(null);
                }
            }
        });

        edEmail.addTextChangedListener(new TextValidator(edEmail) {
            @Override
            @SneakyThrows
            public void validate() {
                if (!edEmail.getText().toString().isEmpty()) {
                    String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!edEmail.getText().toString().matches(emailRegex)) {
                        ChangeEditTextDrawables.changeToErrorDrawable(edEmail, R.drawable.ic_baseline_email_24, SigninActivity.this);
                        edEmail.setError("Valid email required");
                        email = null;
                    } else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edEmail, R.drawable.ic_baseline_email_24, SigninActivity.this);
                        email = edEmail.getText().toString().trim();
                        data.put("email", email);
                    }
                } else {
                    ChangeEditTextDrawables.changeToNormalDrawable(edEmail, R.drawable.ic_baseline_email_24, SigninActivity.this);
                    edEmail.setError(null);
                }
            }
        });

        return email != null
                && password != null;
    }

    @Override
    @SneakyThrows
    public void onSigninSucess(JSONObject response) {
        String role = response.getJSONObject("userData").getJSONArray("roles").getJSONObject(0).getString("name");
        if (role.equals(EUserRoles.ROLE_USER.name())) {
            JSONObject userObject = response.getJSONObject("userData");
            LoggedInUserModel userModel =
                    LoggedInUserModel.builder()
                            .active(userObject.getBoolean("active"))
                            .email(userObject.getString("email"))
                            .firstName(userObject.getString("firstName"))
                            .lastName(userObject.getString("lastName"))
                            .uuid(userObject.getString("uuid"))
                            .build();
            String accessToken = response.getJSONObject("tokenPayload").getString("accessToken");
            sessionManager.setUserRole(role);
            sessionManager.setAccessToken(accessToken);
            sessionManager.setLoggedInUser(userModel.toString());
            finish();
        }
    }

    @Override
    public void onsigninError(VolleyError error) {
        flagErrors.flagApiError(error);
    }

    @Override
    public void onValidationError() {
        Toast.makeText(this, "Input validation error ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showLoading() {
        btnSendLoginRequest.setActivated(false);
        btnSendLoginRequest.setEnabled(false);
        btnSendLoginRequest.setText("Authenticating...");
    }

    @Override
    public void hideLoading() {
        btnSendLoginRequest.setActivated(true);
        btnSendLoginRequest.setEnabled(true);
        btnSendLoginRequest.setText("Sign in");
    }
}