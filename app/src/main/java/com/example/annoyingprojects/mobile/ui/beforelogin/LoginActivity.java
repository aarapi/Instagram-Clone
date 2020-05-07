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




        fragmentSplash = FramgentSplashScreen.newInstance();
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
        return R.layout.activity_sign_in_layout;
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
       if (action == CheckSetup.ServerActions.INSTA_COMMERCE_LANGUAGE_DATA){
           LinkedTreeMap<String, String> resourceList = (LinkedTreeMap<String, String>) data.get(0);
           SavedInformation.getInstance().setResourceList(resourceList);

           Fragment currentFragment = fragmentManager.findFragmentById(android.R.id.content);
           fragmentManager.beginTransaction()
                   .detach(currentFragment)
                   .attach(currentFragment)
                   .commit();

       }
        }


    public AlertDialog getLanguageDialog() {

        final String[] languages = {"sq","en"};
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
