package com.example.connectionframework.requestframework.json;


import java.io.Serializable;

public class JsonData implements Serializable {
	private String __type;

	public JsonData() {
		__type = this.getClass().getSimpleName();
	}
	
	public String get__type() {
		return __type;
	}

	public void set__type(String __type) {
		this.__type = __type;
	}

}