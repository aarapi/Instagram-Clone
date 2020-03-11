package com.example.annoyingprojects.utilities;

import com.example.annoyingprojects.appconfiguration.ApplicationActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.DashboardActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.personaldata.PersonalDataActivity;
import com.example.annoyingprojects.mobile.ui.beforelogin.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class CheckSetup {
    public static Map<Integer, ApplicationActivity> applicationActivityMap;

    public static class ServerActions {
        public static int ANNOYING_PROJECTS_LANGUAGE_DATA = 0;
        public static int ANNOYING_PROJECTS_LOG_IN = 1;
        public static int ANNOYING_PROJECTS_DASHBOARD_ACTIVITY = 2;
        public static int ANNOYING_PROJECTS_PERSONAL_DATA = 3;
    }

    public static class Activities {
        public static int LOG_IN_ACTIVITY = 0;
        public static int DASHBOARD_ACTIVITY = 1;
        public static int PERSONAL_DATA_ACTIVITY = 2;
        public static int HOME_ACTIVITY = 3;
    }


    public static void initializeApplicationActivity() {
        applicationActivityMap = new HashMap<>();
        addApplicationActivityToConfigurationMap(Activities.LOG_IN_ACTIVITY, LoginActivity.class);
        addApplicationActivityToConfigurationMap(Activities.DASHBOARD_ACTIVITY, DashboardActivity.class);
        addApplicationActivityToConfigurationMap(Activities.PERSONAL_DATA_ACTIVITY, PersonalDataActivity.class);
        addApplicationActivityToConfigurationMap(Activities.HOME_ACTIVITY, HomeActivity.class);
    }


    private static void addApplicationActivityToConfigurationMap(
            int activityId, Class<?> activityClass) {
        ApplicationActivity applicationActivity = new ApplicationActivity(
                activityId, activityClass);
        applicationActivityMap.put(applicationActivity.getActivityId(),
                applicationActivity);
    }

}
