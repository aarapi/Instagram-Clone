package com.example.annoyingprojects.utilities;

import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;
import com.example.annoyingprojects.data.User;

import java.util.ArrayList;
import java.util.List;

public class RequestFunction {


    public static Request getLanguageData(int activityId, String languageData){
        List<Object> params = new ArrayList<>();
        params.add(languageData);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_LANGUAGE_DATA, params);
    }
    public static Request signUp(int activityId, User user) {
        List<Object> params = new ArrayList<>();
        params.add(user);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_SIGN_UP, params);
    }
    public static Request loginValidate(int activityId, User user) {
        List<Object> params = new ArrayList<>();
        params.add(user);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_LOG_IN, params);
    }
    public static Request getPostData(int activityId, int scrollTime){
        List<Object> params = new ArrayList<>();
        params.add(scrollTime);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_HOME_DATA, params);
    }
    public static Request getUserProfileData(int activityId, String username){
        List<Object> params = new ArrayList<>();
        params.add(username);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_USER_PROFILE, params);
    }

    public static Request getDashboardData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_DASHBOARD_ACTIVITY, params);
    }

    public static Request getPersonalData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_DASHBOARD_ACTIVITY, params);
    }

    public static Request createNewPost(int activityId, List<String> postData){
        List<Object> params = new ArrayList<>();
        params.add(postData);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_CREATE_NEW_POST, params);

    }
}
