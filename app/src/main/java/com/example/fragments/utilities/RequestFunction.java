package com.example.fragments.utilities;

import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;
import com.example.fragments.data.User;

import java.util.ArrayList;
import java.util.List;

public class RequestFunction {

    public static Request getTest(int activityId, User user) {
        List<Object> params = new ArrayList<>();
        params.add(user);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_LOG_IN, params);
    }
}
