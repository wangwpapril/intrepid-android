package com.intrepid.travel.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class AssistanceProvider implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6491498174329215221L;

	public String id;
	
	public String name;
	
	public String phone;
	
	public AssistanceProvider(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		name = obj.getString("name");
		phone =  obj.getString("phone");
	}

	
}