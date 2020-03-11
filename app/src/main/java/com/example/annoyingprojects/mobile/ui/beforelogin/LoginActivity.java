package com.example.annoyingprojects.mobile.ui.beforelogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import static com.example.annoyingprojects.utilities.CheckSetup.initializeApplicationActivity;

public class LoginActivity extends BaseActivity {
    private Fragment fragmentSplash, fragmentSignIn, fragmentSignUp, fragmentDashboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApplicationActivity();


        sendRequest(RequestFunction.getLanguageData(getActivityId(),
                SavedInformation.getInstance().
                        getPreferenceData(getApplicationContext(), "languageData")));

        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_LOG_IN);
        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_LANGUAGE_DATA);



        fragmentSplash = new FramgentSplashScreen();
        fragmentSignIn = new FragmentLogIn();
        fragmentSignUp = new FragmentSignUp();
        fragmentManager.beginTransaction()
                .add(android.R.id.content, fragmentSplash).commit();

    }

    @Override
    public void initViews() {

    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {

    }

    @Override
    public int getLayoutContent() {
        return R.layout.dashboard_activity;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.LOG_IN_ACTIVITY;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
       if (action == CheckSetup.ServerActions.ANNOYING_PROJECTS_LANGUAGE_DATA){

           LinkedTreeMap<String, String> resourceList = (LinkedTreeMap<String, String>) data.get(0);
           SavedInformation.getInstance().setResourceList(resourceList);

           Fragment currentFragment = fragmentManager.findFragmentById(android.R.id.content);
           fragmentManager.beginTransaction()
                   .detach(currentFragment)
                   .attach(currentFragment)
                   .commit();

       }else if (action == CheckSetup.ServerActions.ANNOYING_PROJECTS_LOG_IN){
           startActivity(CheckSetup.Activities.HOME_ACTIVITY);
       }

    }

    public Fragment getFragmentSplash() {
        return fragmentSplash;
    }

    public Fragment getFragmentSignIn() {
        return fragmentSignIn;
    }

    public Fragment getFragmentSignUp() {
        return fragmentSignUp;
    }

    public AlertDialog getLanguageDialog() {

        final String[] languages = {"Shqip","English"};
        int selectedIndex = -1;

        for (int i = 0; i<languages.length; i++){


            boolean flag = languages[i].equals(SavedInformation.getInstance().getPreferenceData(getApplicationContext(), "languageData"));

            if (flag)
            {
                selectedIndex = i;
                break;
            }
        }


        final int whichBefore = selectedIndex;
        return new AlertDialog.Builder(new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme))
                .setSingleChoiceItems(languages, selectedIndex,
                        (dialog, which) -> {
                            if (whichBefore != which) {
                                List<Object> data = new ArrayList<>();
                                SavedInformation.getInstance().setPreferenceData(getApplicationContext(),languages[which], "languageData");
                                activity.sendRequest(RequestFunction.getLanguageData(activity.getActivityId(),
                                        SavedInformation.getInstance().
                                                getPreferenceData(getApplicationContext(), "languageData")));
                            }
                        })
                .setCancelable(true)
                .create();
    }

}
