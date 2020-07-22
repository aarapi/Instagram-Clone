package com.instacommerce.annoyingprojects.mobile.ui.beforelogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.mobile.basemodels.BaseActivity;
import com.instacommerce.annoyingprojects.repository.LocalServer;
import com.instacommerce.annoyingprojects.utilities.CheckSetup;
import com.instacommerce.annoyingprojects.utilities.RequestFunction;
import com.instacommerce.connectionframework.requestframework.languageData.SavedInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import static com.instacommerce.annoyingprojects.utilities.CheckSetup.initializeApplicationActivity;

public class LoginActivity extends BaseActivity {
    private Fragment fragmentSplash, fragmentSignIn, fragmentSignUp, fragmentDashboard;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApplicationActivity();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

//        sendRequest(RequestFunction.getLanguageData(getActivityId(),
//                SavedInformation.getInstance().
//                        getPreferenceData(getApplicationContext(), "languageData")));

        switchLoginFragment();


    }

    @Override
    public void initViews() {

    }

    @Override
    public void bindEvents() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocalServer.isLogedIn) {
            switchLoginFragment();
            LocalServer.isLogedIn = false;
        }

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

           Fragment currentFragment = fragmentManager.findFragmentById(R.id.container_frame);
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


    void switchLoginFragment() {
        String userToken = SavedInformation.getInstance().getPreferenceData(getBaseContext(), "USER_TOKEN");
        if (userToken == null || "".equals(userToken)) {
            switchFragment(R.id.container_frame, new FragmentLogIn());
        } else {
            switchFragment(R.id.container_frame, new FragmentLoginWithToken());
        }
    }

}
