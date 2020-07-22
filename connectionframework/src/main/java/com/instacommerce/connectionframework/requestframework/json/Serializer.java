package com.instacommerce.connectionframework.requestframework.json;

import com.google.gson.Gson;

public class Serializer 
{
	public static String toJson(Object object)
	{
		Gson gson = new Gson();
		return gson.toJson(object);
	}
}