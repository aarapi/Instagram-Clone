package com.instacommerce.connectionframework.requestframework.languageData;

public enum ResourceKey {


    SignInText("loginText"),
    ForgotPasswordText("forgotPassowrdText"),
    PersonalDataText("personalDataText");



    private final String value;

    ResourceKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
