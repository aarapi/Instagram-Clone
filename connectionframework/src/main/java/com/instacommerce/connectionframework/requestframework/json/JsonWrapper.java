package com.instacommerce.connectionframework.requestframework.json;


import com.instacommerce.connectionframework.requestframework.sender.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;

public class JsonWrapper {

    public static Message getobject(String jsonString){

        Gson gson = new Gson();

        return gson.fromJson(jsonString, Message.class);
    }

    public static Object getobject(String jsonString, Object object){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            object = objectMapper.readValue(jsonString, object.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  object;
    }

    public static Object getobject(Object jsonString, Object object) {

        Gson gson = new Gson();

        object = gson.fromJson(jsonString.toString(), object.getClass());

        return object;
    }
}
