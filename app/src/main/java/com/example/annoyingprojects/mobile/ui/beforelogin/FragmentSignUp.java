package com.example.annoyingprojects.mobile.ui.beforelogin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.FilterModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.LocationUtil;
import com.example.annoyingprojects.utilities.RequestFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FragmentSignUp extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText username, email, password, phoneNumber;

    private TextView signin;
    private TextView tv_sign_up;
    private TextView tv_error;
    private TextView tv_success;

    private RelativeLayout signup;
    private ProgressBar progressbar;
    private Spinner sp_countries;
    private Spinner sp_cities;

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
        phoneNumber = containerView.findViewById(R.id.atv_phone);

        signin = containerView.findViewById(R.id.tv_sign_in);
        tv_sign_up = containerView.findViewById(R.id.tv_sign_up);
        tv_error = containerView.findViewById(R.id.tv_error);
        tv_success = containerView.findViewById(R.id.tv_success);

        signup = containerView.findViewById(R.id.btnSignUp);
        progressbar = containerView.findViewById(R.id.progressbar);
        sp_countries = containerView.findViewById(R.id.sp_countries);
        sp_cities = containerView.findViewById(R.id.sp_cities);



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


    private boolean validateInput(String inName, String inPw, String inEmail, String phoneNmbrString) {

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
        if (phoneNmbrString.isEmpty()){
            phoneNumber.setError("Phone Number is empty.");
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
            final String phoneNumberString = phoneNumber.getText().toString();

            tv_error.setVisibility(View.GONE);

            if (validateInput(inputName, inputPw, inputEmail, phoneNumberString)) {

                UserModel userModel = new UserModel();

                userModel.email = inputEmail;
                userModel.password = inputPw;
                userModel.username = inputName;
                userModel.phoneNumber = phoneNumberString;
                userModel.country = sp_countries.getSelectedItem().toString();
                userModel.city = sp_cities.getSelectedItem().toString();

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
        phoneNumber.setText("");
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
