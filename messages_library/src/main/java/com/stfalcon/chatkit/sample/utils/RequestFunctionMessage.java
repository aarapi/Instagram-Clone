package com.stfalcon.chatkit.sample.utils;

import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;

import java.util.ArrayList;
import java.util.List;

public class RequestFunctionMessage {


    public static Request getMessageUsers(int activityId){
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetupMessages.ServerActions.INSTA_COMMERCE_MESSAGE_USERS, params);
    }

}
