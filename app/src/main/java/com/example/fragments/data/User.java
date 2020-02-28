package com.example.fragments.data;


import com.example.fragments.mobile.basemodels.BaseModel;

public class User extends BaseModel {

    public String userName;
    public String password;
    public String email;

    public String __type = getClass().getTypeName();
}
