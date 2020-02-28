package com.example.fragments.mobile.ui.beforelogin;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragments.R;
import com.example.fragments.mobile.basemodels.BaseActivity;
import com.example.fragments.mobile.basemodels.BaseFragment;

public class FragmentSignUp extends BaseFragment implements View.OnClickListener {
    private ImageView logo, joinus;
    private AutoCompleteTextView username, email, password;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;

    public FragmentSignUp() {
        super(R.layout.fragment_sign_up);
    }

    @Override
    public void initViews() {
        logo = containerView.findViewById(R.id.ivRegLogo);
        joinus = containerView.findViewById(R.id.ivJoinUs);
        username = containerView.findViewById(R.id.atvUsernameReg);
        email = containerView.findViewById(R.id.atvEmailReg);
        password = containerView.findViewById(R.id.atvPasswordReg);
        signin = containerView.findViewById(R.id.tvSignIn);
        signup = containerView.findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public void bindEvents() {
        signup.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
    }


    private boolean validateInput(String inName, String inPw, String inEmail) {

        if (inName.isEmpty()) {
            username.setError("Username is empty.");
            return false;
        }
        if (inPw.isEmpty()) {
            password.setError("Password is empty.");
            return false;
        }
        if (inEmail.isEmpty()) {
            email.setError("Email is empty.");
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        if (v == signup) {
            final String inputName = username.getText().toString().trim();
            final String inputPw = password.getText().toString().trim();
            final String inputEmail = email.getText().toString().trim();

            if (validateInput(inputName, inputPw, inputEmail)) {

            }
        } else if (v == signin) {
            changeFragment("logo_transition", R.id.ivRegLogo, ((LoginActivity) activity).getFragmentSignIn());
        }
    }
}
