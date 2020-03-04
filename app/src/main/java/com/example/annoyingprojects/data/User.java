package com.example.annoyingprojects.data;


import com.example.annoyingprojects.mobile.basemodels.BaseModel;

public class User extends BaseModel {

    public String userName;
    public String password;
    public String email;

    public String __type = getClass().getSimpleName();
}
