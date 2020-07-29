package com.instacommerce.annoyingprojects.utilities;

import com.instacommerce.annoyingprojects.appconfiguration.ApplicationActivity;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.messages.MessagesActivity;
import com.instacommerce.annoyingprojects.mobile.ui.beforelogin.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class CheckSetup {
    public static Map<Integer, ApplicationActivity> applicationActivityMap;

    public static class ServerActions {
        public final static int INSTA_COMMERCE_SIGN_UP = -1;
        public final static int INSTA_COMMERCE_LANGUAGE_DATA = 0;
        public final static int INSTA_COMMERCE_LOG_IN = 1;
        public final static int INSTA_COMMERCE_LOG_IN_WITH_TOKEN = 17;
        public final static int INSTA_COMMERCE_DASHBOARD_ACTIVITY = 2;
        public final static int INSTA_COMMERCE_PERSONAL_DATA = 3;
        public final static int INSTA_COMMERCE_HOME_DATA = 4;
        public final static int INSTA_COMMERCE_SEARCH = 5;
        public final static int INSTA_COMMERCE_USER_PROFILE = 6;
        public final static int INSTA_COMMERCE_CREATE_NEW_POST = 7;
        public final static int INSTA_COMMERCE_MESSAGE_USERS = 8;
        public final static int INSTA_COMMERCE_SET_POST_LIKE = 9;
        public final static int INSTA_COMMERCE_GET_SEARCHED_USERS = 10;
        public final static int INSTA_COMMERCE_DELETE_POST = 11;
        public final static int INSTA_COMMERCE_USER_MESSAGES = 12;
        public final static int INSTA_COMMERCE_SEND_NEW_MESSAGE = 13;
        public final static int INSTA_COMMERCE_SEND_POST_MESSAGE = 14;
        public final static int INSTA_COMMERCE_GET_USER_LIST = 15;
        public final static int INSTA_COMMERCE_EDIT_PROFILE = 16;
        public final static int INSTA_COMMERCE_LOG_OUT = 18;
        public final static int INSTA_COMMERCE_CREATE_NEW_STORY = 19;
        public final static int INSTA_COMMERCE_GET_MOST_SEARCHED_POSTS = 20;
    }

    public static class Activities {
        public static int LOG_IN_ACTIVITY = 0;
        public static int DASHBOARD_ACTIVITY = 1;
        public static int COURSE_EXAMPLE_ACTIVITY = 2;
        public static int HOME_ACTIVITY = 3;
        public static int SINGLE_POST_ACTIVITY = 4;
        public static int MESSAGES_ACTIVITY = 5;
        public static int ADD_NEW_POST_ACTIVITY = 6;
    }


    public static void initializeApplicationActivity() {
        applicationActivityMap = new HashMap<>();
        addApplicationActivityToConfigurationMap(Activities.LOG_IN_ACTIVITY, LoginActivity.class);
        addApplicationActivityToConfigurationMap(Activities.HOME_ACTIVITY, HomeActivity.class);
        addApplicationActivityToConfigurationMap(Activities.MESSAGES_ACTIVITY, MessagesActivity.class);
    }


    private static void addApplicationActivityToConfigurationMap(
            int activityId, Class<?> activityClass) {
        ApplicationActivity applicationActivity = new ApplicationActivity(
                activityId, activityClass);
        applicationActivityMap.put(applicationActivity.getActivityId(),
                applicationActivity);
    }

}
