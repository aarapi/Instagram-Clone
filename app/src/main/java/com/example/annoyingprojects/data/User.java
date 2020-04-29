package com.example.annoyingprojects.data;


import com.example.annoyingprojects.mobile.basemodels.BaseModel;

public class User extends BaseModel {


    public String email;
    public String password;
    public String username;
    public String userImage;



    public String __type = getClass().getSimpleName();
}
