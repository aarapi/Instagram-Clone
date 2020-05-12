package com.example.annoyingprojects.data;

import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class MessageUsersModel implements Serializable {

    private int id;
    private String username_from;
    private String username_to;
    private boolean online;
    private String avatar;
    private String avatarFrom;
    private String avatarTo;

    public int getId() {
        return id;
    }

    public String getUsernameTo() {
        return username_to;
    }


    public String getUsernameFrom() {
        return username_from;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }

    public void setUsername_from(String username_from) {
        this.username_from = username_from;
    }

    public void setUsername_to(String username_to) {
        this.username_to = username_to;
    }

    public String getAvatarFrom() {
        return avatarFrom;
    }

    public void setAvatarFrom(String avatarFrom) {
        this.avatarFrom = avatarFrom;
    }

    public String getAvatarTo() {
        return avatarTo;
    }

    public void setAvatarTo(String avatarTo) {
        this.avatarTo = avatarTo;
    }
}
