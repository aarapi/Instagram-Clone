package com.example.annoyingprojects.repository;

import android.content.Context;

import com.example.annoyingprojects.data.MessageUsersModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.octopepper.mediapickerinstagram.commons.models.CategoryModel;

import java.util.ArrayList;

public class LocalServer {
    private  Context context;
    private static LocalServer localServerContext;
    private static LocalServer localServer;
    private LocalServer(Context context) {
        this.context = context;
    }

    public LocalServer() {
    }

    private ArrayList<MessageUsersModel> messageUsersModels;
    private ArrayList<UserModel> lastRecentSearchedUsers = new ArrayList<>();
    private ArrayList<UserModel> userList = new ArrayList<>();
    private ArrayList<CategoryModel> categoryModels = new ArrayList<>();


    private boolean sendNewMessage = false;

    public static LocalServer getInstance(Context context){
        if (localServerContext == null) {
            localServerContext = new LocalServer(context);
        }

        return localServerContext;
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

    public ArrayList<UserModel> getLastRecentSearchedUsers() {
        return lastRecentSearchedUsers;
    }

    public void setLastRecentSearchedUsers(UserModel userModel) {
        if (!lastRecentSearchedUsers.contains(userModel)) {
            lastRecentSearchedUsers.add(userModel);
        }
    }

    public boolean isSendNewMessage() {
        return sendNewMessage;
    }

    public void setSendNewMessage(boolean sendNewMessage) {
        this.sendNewMessage = sendNewMessage;
    }

    public void setUserList(ArrayList<UserModel> userList) {
        this.userList = userList;
    }

    public ArrayList<UserModel> getUserList() {
        return userList;
    }

    public ArrayList<CategoryModel> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }
}
