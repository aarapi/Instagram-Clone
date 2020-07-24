package com.instacommerce.annoyingprojects.mobile.ui.beforelogin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.UserModel;
import com.instacommerce.annoyingprojects.mobile.basemodels.BaseFragment;
import com.instacommerce.annoyingprojects.utilities.FragmentUtil;
import com.instacommerce.annoyingprojects.utilities.LocationUtil;
import com.instacommerce.annoyingprojects.utilities.RequestFunction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import gujc.directtalk9.common.Util9;
import gujc.directtalk9.model.User;

public class FragmentSignUp extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText username, email, password, et_confirmPwd;

    private TextView signin;
    private TextView tv_sign_up;
    private TextView tv_error;
    private TextView tv_success;

    private RelativeLayout signup;
    private ProgressBar progressbar;
    private Spinner sp_countries;
    private Spinner sp_cities;

    private SharedPreferences sharedPreferences;

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
        et_confirmPwd = containerView.findViewById(R.id.et_confirmPwd);

        signin = containerView.findViewById(R.id.tv_sign_in);
        tv_sign_up = containerView.findViewById(R.id.tv_sign_up);
        tv_error = containerView.findViewById(R.id.tv_error);
        tv_success = containerView.findViewById(R.id.tv_success);

        signup = containerView.findViewById(R.id.btnSignUp);
        progressbar = containerView.findViewById(R.id.progressbar);
        sp_countries = containerView.findViewById(R.id.sp_countries);
        sp_cities = containerView.findViewById(R.id.sp_cities);


        sharedPreferences = getActivity().getSharedPreferences("gujc", Activity.MODE_PRIVATE);
    }

    @Override
    public void bindEvents() {
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        setCountriesList();
    }

    public void setCountriesList() {
        String[] countries = LocationUtil.newInstance(getContext()).getCountries();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        sp_countries.setAdapter(countryAdapter);
        sp_countries.setOnItemSelectedListener(this);

    }

    public void setCityList(String countryName) {
        ArrayList<String> cites = LocationUtil.newInstance(getContext())
                .getListOfCities(countryName);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, cites);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        sp_cities.setAdapter(cityAdapter);
        sp_cities.setSelection(1);
        cityAdapter.notifyDataSetChanged();

    }


    private boolean validateInput(String inName, String inPw, String inEmail, String inPwdConfirm) {

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
        if (!(!TextUtils.isEmpty(inEmail) && Patterns.EMAIL_ADDRESS.matcher(inEmail).matches())) {
            email.setError("Email is not valid.");
            return false;
        }
        if (!inPw.equals(inPwdConfirm)) {
            et_confirmPwd.setError("Password is not correct.");
            return false;
        }
        if (inPw.length() < 8) {
            password.setError("Password must contain at least 8 characters.");
            return false;
        }


        return true;
    }


    @Override
    public void onClick(View v) {
        if (v == signup) {
            final String inputName = username.getText().toString();
            final String inputPw = password.getText().toString();
            final String inputEmail = email.getText().toString();
            final String inputPwdConfirm = et_confirmPwd.getText().toString();

            tv_error.setVisibility(View.GONE);

            if (validateInput(inputName, inputPw, inputEmail, inputPwdConfirm)) {

                UserModel userModel = new UserModel();

                userModel.email = inputEmail;
                userModel.password = inputPw;
                userModel.username = inputName;
                userModel.phoneNumber = "";
                userModel.country = sp_countries.getSelectedItem().toString();
                userModel.city = sp_cities.getSelectedItem().toString();

                progressbar.setVisibility(View.VISIBLE);
                tv_sign_up.setVisibility(View.GONE);
                sendRequest(RequestFunction.signUp(0, userModel));

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(userModel.email, userModel.password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sharedPreferences.edit().putString("user_id", userModel.email).commit();
                                    final String uid = FirebaseAuth.getInstance().getUid();

                                    User user = new User();
                                    user.setUid(uid);
                                    user.setUserid(userModel.email);
                                    user.setUsernm(userModel.username);
                                    user.setUsermsg("...");

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("users").document(uid)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
//                                                    Util9.showMessage(getContext(), "Register sucessfully");
                                                }
                                            });
                                } else {
                                    Util9.showMessage(getContext(), task.getException().getMessage());
                                }
                            }
                        });
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
        et_confirmPwd.setText("");
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setCityList(LocationUtil.newInstance(getContext()).getCountries()[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
