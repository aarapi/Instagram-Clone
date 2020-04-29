package com.example.annoyingprojects.repository;

import android.content.Context;

import com.example.annoyingprojects.data.User;
import com.example.connectionframework.requestframework.languageData.SavedInformation;

public class LocalServer {
    private  Context context;
    private static LocalServer localServer;

    private LocalServer(Context context) {
        this.context = context;
    }

    public static LocalServer getInstance(Context context){
        if (localServer == null){
            localServer = new LocalServer(context);
        }

        return localServer;
    }


    public  User getUser(){
        return  (User) SavedInformation.getInstance().getPreferenceData(context, "user", User.class);
    }
}
