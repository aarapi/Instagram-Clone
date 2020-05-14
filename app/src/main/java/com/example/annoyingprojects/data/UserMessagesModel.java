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
}
