package com.example.annoyingprojects.mobile.ui.beforelogin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
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
import com.example.connectionframework.requestframework.languageData.ResourceKey;
import com.example.connectionframework.requestframework.languageData.SavedInformation;

import java.util.ArrayList;
import java.util.List;

public class FragmentLogIn extends BaseFragment implements View.OnClickListener{
    private ImageView logo, ivSignIn, btnTwitter,iv_language;
    private AutoCompleteTextView email, password;
    private TextView forgotPass, signUp, tvForgotPass;
    private Button btnSignIn;
    private ProgressDialog progressDialog;
    private Fragment fragmentLogIn;
    private AlertDialog alertDialog;

    public FragmentLogIn() {
        super(R.layout.fragement_log_in);

    }

    @Override
    public void initViews() {

        iv_language = containerView.findViewById(R.id.iv_language);
        logo = containerView.findViewById(R.id.ivLogLogo);
        ivSignIn = containerView.findViewById(R.id.ivSignIn);
        btnTwitter = containerView.findViewById(R.id.ivFacebook);
        email = containerView.findViewById(R.id.atvEmailLog);
        password = containerView.findViewById(R.id.atvPasswordLog);
        forgotPass = containerView.findViewById(R.id.tvForgotPass);
        signUp = containerView.findViewById(R.id.tvSignIn);
        btnSignIn = containerView.findViewById(R.id.btnSignIn);
        progressDialog = new ProgressDialog(getContext());

        tvForgotPass = containerView.findViewById(R.id.tvForgotPass);
    }

    @Override
    public void bindEvents() {
        forgotPass.setOnClickListener(this);
        signUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        iv_language.setOnClickListener(this);
    }

    @Override
    public void setViews() {

        tvForgotPass.setText(SavedInformation.getInstance().getResource(ResourceKey.ForgotPasswordText));

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

        }else if (v == iv_language){
            alertDialog = getLanguageDialog();
            alertDialog.show();
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

    private AlertDialog getLanguageDialog() {

        final String[] languages = {"Shqip","English"};
        int selectedIndex = -1;

        for (int i = 0; i<languages.length; i++){


            boolean flag = languages[i].equals(SavedInformation.getInstance().getPreferenceData(getContext(), "languageData"));

            if (flag)
            {
                selectedIndex = i;
                break;
            }
        }


        final int whichBefore = selectedIndex;
        return new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppTheme))
                .setSingleChoiceItems(languages, selectedIndex,
                        (dialog, which) -> {
                            if (whichBefore != which) {
                                List<Object> data = new ArrayList<>();
                                SavedInformation.getInstance().setPreferenceData(getContext(),languages[which], "languageData");
                                activity.sendRequest(RequestFunction.getLanguageData(activity.getActivityId(),
                                        SavedInformation.getInstance().
                                                getPreferenceData(getContext(), "languageData")));

                                alertDialog.dismiss();
                              }
                        })
                .setCancelable(true)
                .create();
    }

    public AlertDialog getAlertDialog(){
        return alertDialog;
    }


}
