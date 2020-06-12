package com.example.annoyingprojects.data;

import java.sql.Date;
import java.sql.Timestamp;

public class UserMessagesModel {

    private int userMessagesId;
    private String usernamFrom;
    private String usernameTo;
    private String message;
    private Timestamp messageTime;
    private boolean isPostMessage;
    private String postUsername;
    private String postImage;
    private String postUserImg;
    private String postId;

    public int getUserMessagesId() {
        return userMessagesId;
    }

    public void setUserMessagesId(int userMessagesId) {
        this.userMessagesId = userMessagesId;
    }

    public String getUsernamFrom() {
        return usernamFrom;
    }

    public void setUsernamFrom(String usernamFrom) {
        this.usernamFrom = usernamFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Timestamp messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isPostMessage() {
        return isPostMessage;
    }
    public void setPostMessage(boolean postMessage) {
        isPostMessage = postMessage;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserImg() {
        return postUserImg;
    }

    public void setPostUserImg(String postUserImg) {
        this.postUserImg = postUserImg;
    }
}
