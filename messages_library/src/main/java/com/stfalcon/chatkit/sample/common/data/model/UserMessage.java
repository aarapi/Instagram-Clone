package com.stfalcon.chatkit.sample.common.data.model;

import com.stfalcon.chatkit.commons.models.IUser;

/*
 * Created by troy379 on 04.04.17.
 */
public class UserMessage implements IUser {

    private String id;
    private String usernameTo;
    private String usernameFrom;
    private String avatar;
    private boolean online;

    public UserMessage(String id, String usernameTo,String usernameFrom, String avatar, boolean online) {
        this.id = id;
        this.usernameTo = usernameTo;
        this.usernameFrom = usernameFrom;
        this.avatar = avatar;
        this.online = online;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsernameTo() {
        return usernameTo;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
