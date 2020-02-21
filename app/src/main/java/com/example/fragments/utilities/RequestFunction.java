package com.example.fragments.utilities;

import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;

import java.util.ArrayList;
import java.util.List;

public class RequestFunction {

    public static Request getTest(int activityId){
        List<Object> params = new ArrayList<>();

        return RequestFunctions.createRequest(activityId, CheckSetup.LocalActions.ANNOYING_PROJECTS_MAIN_ACTIVITY, params);
    }
}
