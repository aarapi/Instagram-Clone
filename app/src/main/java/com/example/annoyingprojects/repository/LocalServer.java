package com.example.annoyingprojects.repository;

import android.content.Context;

import com.example.annoyingprojects.data.MessageUsersModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.connectionframework.requestframework.languageData.SavedInformation;

import java.util.ArrayList;

public class LocalServer {
    private  Context context;
    private static LocalServer localServer;
    private LocalServer(Context context) {
        this.context = context;
    }

    public LocalServer() {
    }

    private ArrayList<MessageUsersModel> messageUsersModels;
    private ArrayList<UserModel> userModels = new ArrayList<>();

    public static LocalServer getInstance(Context context){
        if (localServer == null){
            localServer = new LocalServer(context);
        }

        return localServer;
    }

    public static LocalServer newInstance() {
        if (localServer == null) {
            localServer = new LocalServer();
        }
        return localServer;
    }

    public UserModel getUser() {
        return (UserModel) SavedInformation.getInstance().getPreferenceData(context, "user", UserModel.class);
    }

    public ArrayList<MessageUsersModel> getMessageUsersModels() {
        return messageUsersModels;
    }

    public void setMessageUsersModels(ArrayList<MessageUsersModel> messageUsersModels) {
        this.messageUsersModels = messageUsersModels;
    }

    public ArrayList<UserModel> getUserModels() {
        return userModels;
    }

    public void setUserModels(UserModel userModel) {
        if (!userModels.contains(userModel)) {
            userModels.add(userModel);
        }
    }
}
