package com.instacommerce.connectionframework.requestframework.sender;


import com.instacommerce.connectionframework.requestframework.constants.MessagingFrameworkConstant;

import java.util.ArrayList;
import java.util.List;

public class RequestFunctions {


    public static Request createRequest(int activityId, int actionCode, List<Object> data){
        if(data == null)
            data = new ArrayList<>();

        Request request = new Request(MessagingFrameworkConstant.ROUTING_ADDRESS.CLIENT,
                MessagingFrameworkConstant.ROUTING_ADDRESS.SERVER, activityId, actionCode, data,
                MessagingFrameworkConstant.STATUS_CODES.Success, MessagingFrameworkConstant.ANIMATION_TYPES.OpenNewPage);

        return request;
    }






}
