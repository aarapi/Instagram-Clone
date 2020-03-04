package com.example.annoyingprojects.appconfiguration;


public class ApplicationActivity {
    private int activityId;
    private Class<?> activityClass;

    public ApplicationActivity(int activityId, Class<?> activityClass) {
        this.activityId = activityId;
        this.activityClass = activityClass;
    }

    public int getActivityId() {
        return this.activityId;
    }

    public Class<?> getActivityClass() {
        return this.activityClass;
    }
}
