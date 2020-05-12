package com.example.annoyingprojects.mobile.ui.beforelogin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;

import java.util.List;

public class FragmentSignUp extends BaseFragment implements View.OnClickListener {
    private EditText username, email, password;

    private TextView signin;
    private TextView tv_sign_up;
    private TextView tv_error;
    private TextView tv_success;

    private RelativeLayout signup;
    private ProgressBar progressbar;

    public static FragmentSignUp newInstance(Bundle args){
        FragmentSignUp fragmentSignUp = new FragmentSignUp();
        fragmentSignUp.setArguments(args);
        return fragmentSignUp;
    }
    @Override
    public void initViews() {
        username = containerView.findViewById(R.id.atvUsernameReg);
        email = containerView.findViewById(R.id.atvEmailReg);
        password = containerView.findViewById(R.id.atvPasswordReg);

        signin = containerView.findViewById(R.id.tv_sign_in);
        tv_sign_up = containerView.findViewById(R.id.tv_sign_up);
        tv_error = containerView.findViewById(R.id.tv_error);
        tv_success = containerView.findViewById(R.id.tv_success);

        signup = containerView.findViewById(R.id.btnSignUp);
        progressbar = containerView.findViewById(R.id.progressbar);

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

            tv_error.setVisibility(View.GONE);

            if (validateInput(inputName, inputPw, inputEmail)) {

                UserModel userModel = new UserModel();

                userModel.email = inputEmail;
                userModel.password = inputPw;
                userModel.username = inputName;

                progressbar.setVisibility(View.VISIBLE);
                tv_sign_up.setVisibility(View.GONE);
                sendRequest(RequestFunction.signUp(0, userModel));
            }
        } else if (v == signin) {
            FragmentUtil.switchContent(R.id.container_frame, FragmentUtil.LOG_IN_FRAGMENT, activity, null);
        }
        tv_success.setVisibility(View.GONE);
        tv_error.setVisibility(View.GONE);
    }


    @Override
    public void onDataReceive(int action, List<Object> data) {
        progressbar.setVisibility(View.GONE);
        tv_sign_up.setVisibility(View.VISIBLE);
        tv_success.setVisibility(View.VISIBLE);

        tv_success.setText((String) data.get(0));
        username.setText("");
        email.setText("");
        password.setText("");
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        tv_error.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        tv_sign_up.setVisibility(View.VISIBLE);

        String errorString = (String) data.get(0);
        tv_error.setText(errorString);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_signup_layout;
    }


}
