package com.example.connectionframework.requestframework.json;



import com.example.connectionframework.requestframework.sender.Message;
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
}
