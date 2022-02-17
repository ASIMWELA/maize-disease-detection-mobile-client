package com.detection.diseases.maize.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.ChangeEditTextDrawables;
import com.detection.diseases.maize.helpers.TextValidator;
import com.detection.diseases.maize.ui.signup.SignUpActivity;
import com.google.android.material.chip.Chip;
import com.kusu.loadingbutton.LoadingButton;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class SigninActivity extends Fragment implements SigninContract.View{

    private SigninPresenter signinPresenter;
    private EditText edPassword, edEmail;
    private Chip cpBbacArrow;
    private TextView tvOpenSignUp;
    private LoadingButton btnSendLoginRequest;
    private String email, password;
    JSONObject data = new JSONObject();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signin, container, false);
        initViews(root);
        signinPresenter = new SigninPresenter(this, requireContext());

        cpBbacArrow.setOnClickListener(v->{
            requireActivity().onBackPressed();
        });

        tvOpenSignUp.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), SignUpActivity.class);
            startActivity(intent);
        });
        signinPresenter.initInputValidation();
        btnSendLoginRequest.setOnClickListener(v->signinPresenter.sendSignRequest(data));
        return root;
    }

    private void initViews(View view){
        edEmail = view.findViewById(R.id.sign_in_activity_email);
        edPassword = view.findViewById(R.id.sign_in_tv_password);
        cpBbacArrow = view.findViewById(R.id.sign_in_cp_back);
        tvOpenSignUp = view.findViewById(R.id.sign_in_tv_open_sign_up);
        btnSendLoginRequest = view.findViewById(R.id.sign_in_activity_btn_submit);
    }

    @Override
    public boolean validateInput() {
        edPassword.addTextChangedListener(new TextValidator(edPassword) {
            @Override
            @SneakyThrows
            public void validate() {
                if (!edPassword.getText().toString().isEmpty()) {
                    if (edPassword.getText().toString().trim().length() < 5) {
                        ChangeEditTextDrawables.changeToErrorDrawable(edPassword, R.drawable.ic_baseline_lock_24, requireContext());
                        edPassword.setError("Valid password required");
                        password = null;
                    } else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edPassword, R.drawable.ic_baseline_lock_24, requireContext());
                        password = edPassword.getText().toString().trim();
                        data.put("password", password);
                    }
                }else{
                    ChangeEditTextDrawables.changeToNormalDrawable(edPassword, R.drawable.ic_baseline_lock_24, requireContext());
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
                    if(!edEmail.getText().toString().matches(emailRegex)){
                        ChangeEditTextDrawables.changeToErrorDrawable(edEmail, R.drawable.ic_baseline_email_24, requireContext());
                        edEmail.setError("Valid email required");
                        email = null;
                    }else {
                        ChangeEditTextDrawables.changeToNormalDrawable(edEmail, R.drawable.ic_baseline_email_24, requireContext());
                        email = edEmail.getText().toString().trim();
                        data.put("email", email);
                    }
                }else {
                    ChangeEditTextDrawables.changeToNormalDrawable(edEmail, R.drawable.ic_baseline_email_24, requireContext());
                    edEmail.setError(null);
                }
            }
        });

        return email !=null
                && password != null;
    }

    @Override
    public void onSigninSucess(JSONObject response) {
        Toast.makeText(requireContext(), "SUCESS:  "+response.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onsigninError(VolleyError error) {
        Toast.makeText(requireContext(), "Error : "+error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationError() {
        Toast.makeText(requireContext(), "Input validation error ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showLoading() {
        btnSendLoginRequest.setActivated(false);
        btnSendLoginRequest.setEnabled(false);
        btnSendLoginRequest.showLoading();
    }

    @Override
    public void hideLoading() {
        btnSendLoginRequest.setActivated(true);
        btnSendLoginRequest.setEnabled(true);
        btnSendLoginRequest.hideLoading();
    }
}