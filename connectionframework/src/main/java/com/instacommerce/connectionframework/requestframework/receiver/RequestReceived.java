package com.instacommerce.connectionframework.requestframework.receiver;

import java.util.List;

public interface RequestReceived {

    public void onRequestReceived(int p_action, List<Object> data);
    public void onErrorReceived(int p_action, List<Object> data);

    public void onErrorReceived(int p_action, List<Object> data, int status);
}

