package com.example.annoyingprojects.utilities;

import com.example.annoyingprojects.data.FilterModel;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.UserMessagesModel;
import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;
import com.example.annoyingprojects.data.UserModel;

import java.util.ArrayList;
import java.util.List;

public class RequestFunction {

    public static String username;

    public static Request getLanguageData(int activityId, String languageData){
        List<Object> params = new ArrayList<>();
        params.add(languageData);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_LANGUAGE_DATA, params);
    }

    public static Request signUp(int activityId, UserModel userModel) {
        List<Object> params = new ArrayList<>();
        params.add(userModel);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_SIGN_UP, params);
    }

    public static Request loginValidate(int activityId, UserModel userModel) {
        List<Object> params = new ArrayList<>();
        params.add(userModel);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_LOG_IN, params);
    }

    public static Request loginWithToken(int activityId, String userToken) {
        List<Object> params = new ArrayList<>();
        params.add(userToken);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_LOG_IN_WITH_TOKEN, params);
    }

    public static Request logOut(int activityId, String userToken) {
        List<Object> params = new ArrayList<>();
        params.add(userToken);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_LOG_OUT, params);
    }

    public static Request getPostData(int activityId, int scrollTime, FilterModel filterModel, String searchString) {
        List<Object> params = new ArrayList<>();
        params.add(scrollTime);
        params.add(username);
        params.add(filterModel.country);
        params.add(filterModel.city);
        params.add(filterModel.category);
        params.add(filterModel.allCategories);
        params.add(searchString);

        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_HOME_DATA, params);
    }

    public static Request setPostLike(int activityId, int postId, boolean setChecked) {
        List<Object> params = new ArrayList<>();
        params.add(postId);
        params.add(setChecked);
        params.add(username);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_SET_POST_LIKE, params);
    }
    public static Request getUserProfileData(int activityId, String username){
        List<Object> params = new ArrayList<>();
        params.add(username);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_USER_PROFILE, params);
    }

    public static Request getSearchedUSer(int activityId, String searchString) {
        List<Object> params = new ArrayList<>();
        params.add(searchString);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_GET_SEARCHED_USERS, params);
    }

    public static Request getDashboardData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_DASHBOARD_ACTIVITY, params);
    }

    public static Request getPersonalData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_DASHBOARD_ACTIVITY, params);
    }

    public static Request createNewPost(int activityId, List<String> postData, String country, String city) {
        List<Object> params = new ArrayList<>();
        params.add(postData);
        params.add(username);
        params.add(country);
        params.add(city);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_CREATE_NEW_POST, params);
    }

    public static Request deletePost(int activityId, int postID) {
        List<Object> params = new ArrayList<>();
        params.add(postID);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_DELETE_POST, params);

    }
    public static Request getMessageUsers(int activityId) {
        List<Object> params = new ArrayList<>();
        params.add(username);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_MESSAGE_USERS, params);

    }

    public static Request getUserMessages(int activityId, String usernameTo, String usernameFrom) {
        List<Object> params = new ArrayList<>();
        params.add(usernameTo);
        params.add(usernameFrom);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_USER_MESSAGES, params);

    }

    public static Request sendNewMessage(int activityId, UserMessagesModel userMessagesModel) {
        List<Object> params = new ArrayList<>();

        params.add(userMessagesModel.getUsernamFrom());
        params.add(userMessagesModel.getUsernameTo());
        params.add(userMessagesModel.getMessage());

        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_SEND_NEW_MESSAGE, params);

    }

    public static Request sendPostMessage(int activityId, List<UserModel> userModels, String messageString, PostModel postModel) {
        List<Object> params = new ArrayList<>();

        params.add(userModels);
        params.add(messageString);
        params.add(postModel);
        params.add(username);

        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_SEND_POST_MESSAGE, params);

    }
    public static Request editProfile(int activityId, UserModel userModel, String encodedImage) {
        List<Object> params = new ArrayList<>();
        params.add(userModel);
        params.add(encodedImage);
        params.add(username);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.INSTA_COMMERCE_EDIT_PROFILE, params);

    }

}
