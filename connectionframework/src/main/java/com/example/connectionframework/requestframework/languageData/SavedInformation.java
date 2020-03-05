package com.example.connectionframework.requestframework.languageData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.internal.LinkedTreeMap;

public class SavedInformation {
    private static SavedInformation savedInformation;
    private LinkedTreeMap<String , String> resourceList;


    public static SavedInformation getInstance(){
        if (savedInformation == null){
            savedInformation = new SavedInformation();
        }

        return savedInformation;
    }

    public void setResourceList( LinkedTreeMap<String , String> resourceList){
        this.resourceList = resourceList;
    }

    public  LinkedTreeMap<String , String>  getResourceList(){
        return resourceList;
    }

    public String getResource(String resourceKey){

        String temp = resourceList.get(resourceKey);

        if (temp == null)
            temp = "";

        return temp;
    }


    public String getResource(ResourceKey resourceKey){
        String result = getResource(resourceKey.getValue());

        return result;
    }


    public void setPreferenceData(Context context, String languageValue, String preferenceKey){
        SharedPreferences prefs =  context.getSharedPreferences("PREFERENCE_NAME",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(preferenceKey, languageValue);
        editor.apply();
    }

    public String getPreferenceData(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences("PREFERENCE_NAME",
                Context.MODE_PRIVATE);
        String name = preferences.getString(key, "");

        return name;
    }


}
