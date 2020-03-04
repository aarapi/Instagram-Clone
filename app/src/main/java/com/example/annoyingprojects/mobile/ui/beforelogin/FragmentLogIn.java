package com.example.annoyingprojects.mobile.ui.beforelogin;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.User;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;

public class FragmentLogIn extends BaseFragment implements View.OnClickListener {
    private ImageView logo, ivSignIn, btnTwitter;
    private AutoCompleteTextView email, password;
    private TextView forgotPass, signUp;
    private Button btnSignIn;
    private ProgressDialog progressDialog;
    private Fragment fragmentLogIn;

    public FragmentLogIn() {
        super(R.layout.fragement_log_in);

    }

    @Override
    public void initViews() {
        logo = containerView.findViewById(R.id.ivLogLogo);
        ivSignIn = containerView.findViewById(R.id.ivSignIn);
        btnTwitter = containerView.findViewById(R.id.ivFacebook);
        email = containerView.findViewById(R.id.atvEmailLog);
        password = containerView.findViewById(R.id.atvPasswordLog);
        forgotPass = containerView.findViewById(R.id.tvForgotPass);
        signUp = containerView.findViewById(R.id.tvSignIn);
        btnSignIn = containerView.findViewById(R.id.btnSignIn);
        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public void bindEvents() {
        forgotPass.setOnClickListener(this);
        signUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void setViews() {

    }


    @Override
    public void onClick(View v) {
        if (v == btnSignIn) {
            String inEmail = email.getText().toString();
            String inPassword = password.getText().toString();

            if (validateInput(inEmail, inPassword)) {
                User user = new User();
                user.email = inEmail;
                user.password = inPassword;

                signUser(user);
            }
        } else if (v == signUp) {
            changeFragment("logo_transition", R.id.ivLogLogo, ((LoginActivity) activity).getFragmentSignUp());
        } else if (v == forgotPass) {

        }

    }


    public void signUser(User user) {
        startActivity(CheckSetup.Activities.DASHBOARD_ACTIVITY);
//        activity.sendRequest(RequestFunction.loginValidate(activity.getActivityId(), user));
    }


    public boolean validateInput(String inemail, String inpassword) {

        if (inemail.isEmpty()) {
            email.setError("Email field is empty.");
            return false;
        }
        if (inpassword.isEmpty()) {
            password.setError("Password is empty.");
            return false;
        }

        return true;
    }
}
