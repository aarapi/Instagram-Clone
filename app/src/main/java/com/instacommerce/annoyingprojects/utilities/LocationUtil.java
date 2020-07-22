package com.instacommerce.annoyingprojects.utilities;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LocationUtil {
    private static LocationUtil locationUtil;
    private LocationUtil(){}

    private static Context mContext;

    public static LocationUtil newInstance(Context context){
        mContext = context;
        if (locationUtil == null)
        {
            locationUtil = new LocationUtil();
        }

        return locationUtil;
    }

    private  String[] countries =
            new String[]{"Albania","Germany","France","United Kingdom","Greece","Italy","Kosovo"};


    public String[] getCountries() {
        return countries;
    }
    public ArrayList<String> getListOfCities(String countryName){
        ArrayList<String> citiesList = new ArrayList<>();
        Gson gson = new Gson();
        Type countryType = new TypeToken<ArrayList<Country>>() {
        }.getType();

        String json = null;
        try {
            InputStream is = mContext.getAssets().open(countryName+".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        ArrayList<Country> countries = gson.fromJson(json, countryType);
        int size = countries.size();

        for (int i=0; i<size; i++){
            citiesList.add(countries.get(i).city);
        }

        return citiesList;
    }

    class Country{
        public String city;
    }
}
