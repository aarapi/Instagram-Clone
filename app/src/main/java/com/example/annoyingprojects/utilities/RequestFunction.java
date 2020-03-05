package com.example.annoyingprojects.utilities;

import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;
import com.example.annoyingprojects.data.User;

import java.util.ArrayList;
import java.util.List;

public class RequestFunction {


    public static Request getLanguageData(int activityId, String languageData){
        List<Object> params = new ArrayList<>();
        params.add(languageData);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_LANGUAGE_DATA, params);
    }
    public static Request loginValidate(int activityId, User user) {
        List<Object> params = new ArrayList<>();
        params.add(user);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_LOG_IN, params);
    }

    public static Request getDashboardData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_DASHBOARD_ACTIVITY, params);
    }

    public static Request getPersonalData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_DASHBOARD_ACTIVITY, params);
    }
}
