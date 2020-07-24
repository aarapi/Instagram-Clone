package com.instacommerce.annoyingprojects.data;


import com.instacommerce.annoyingprojects.mobile.basemodels.BaseModel;

public class UserModel extends BaseModel {


    public String email;
    public String password;
    public String username;
    public String userImage;
    public String phoneNumber;
    public String country;
    public String city;
    public boolean showContact;





    public String __type = getClass().getSimpleName();
}
