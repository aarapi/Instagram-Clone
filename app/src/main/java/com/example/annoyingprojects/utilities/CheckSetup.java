package com.example.annoyingprojects.utilities;

import com.example.annoyingprojects.appconfiguration.ApplicationActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.DashboardActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.messages.MessagesActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.personaldata.CourseExampleActivity;
import com.example.annoyingprojects.mobile.ui.beforelogin.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class CheckSetup {
    public static Map<Integer, ApplicationActivity> applicationActivityMap;

    public static class ServerActions {
        public final static int INSTA_COMMERCE_SIGN_UP = -1;
        public final static int INSTA_COMMERCE_LANGUAGE_DATA = 0;
        public final static int INSTA_COMMERCE_LOG_IN = 1;
        public final static int INSTA_COMMERCE_DASHBOARD_ACTIVITY = 2;
        public final static int INSTA_COMMERCE_PERSONAL_DATA = 3;
        public final static int INSTA_COMMERCE_HOME_DATA = 4;
        public final static int INSTA_COMMERCE_SEARCH = 5;
        public final static int INSTA_COMMERCE_USER_PROFILE = 6;
        public final static int INSTA_COMMERCE_CREATE_NEW_POST = 7;
    }

    public static class Activities {
        public static int LOG_IN_ACTIVITY = 0;
        public static int DASHBOARD_ACTIVITY = 1;
        public static int COURSE_EXAMPLE_ACTIVITY = 2;
        public static int HOME_ACTIVITY = 3;
        public static int SINGLE_POST_ACTIVITY = 4;
        public static int MESSAGES_ACTIVITY = 5;
    }


    public static void initializeApplicationActivity() {
        applicationActivityMap = new HashMap<>();
        addApplicationActivityToConfigurationMap(Activities.LOG_IN_ACTIVITY, LoginActivity.class);
        addApplicationActivityToConfigurationMap(Activities.DASHBOARD_ACTIVITY, DashboardActivity.class);
        addApplicationActivityToConfigurationMap(Activities.COURSE_EXAMPLE_ACTIVITY, CourseExampleActivity.class);
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
