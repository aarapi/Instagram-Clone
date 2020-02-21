package com.example.connectionframework.requestframework.sender;


import com.example.connectionframework.requestframework.constants.MessagingFrameworkConstant;

import java.util.List;

public class Request extends Message {

    public Request(int source, int target, int relatedView, int action, final List<Object> data, int statusCode, int animationType) {
        super(source,target,relatedView,action,data,statusCode,animationType);
    }

    /**
     * Creates a request suitable to navigate to another activity,
     *
     * @param currentActivityId from we want to navigate
     * @param newActivityAction where we want to navigate
     * @param data              to put to new activity
     * @return built request
     */
    public static Request navigateToRequest(int currentActivityId, int newActivityAction, List<Object> data) {
        return new Request(
                MessagingFrameworkConstant.ROUTING_ADDRESS.CLIENT_STACK_NAVIGATOR,
                MessagingFrameworkConstant.ROUTING_ADDRESS.LOOP_BACK_SERVICE,
                currentActivityId,
                newActivityAction,
                data
        );
    }

    public Request(int source, int target, int relatedView, int action, List<Object> data) {
        super(source, target, relatedView, action, data, MessagingFrameworkConstant.STATUS_CODES.Success, MessagingFrameworkConstant.ANIMATION_TYPES.OpenNewPage);
    }
}
