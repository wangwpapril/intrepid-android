package com.intrepid.travel.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4965695632276223806L;
	
	public String id;
	
	public String name;
	
	public String email;
	
	public String groupNumber;
	
	public String parentId;
	
	public String createdAt;
	
	public String updatedAt;
	
	public String countryId;
	
	public Parent(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		name = obj.getString("name");
		email =  obj.getString("email");
		groupNumber =  obj.getString("group_number");
		parentId =  obj.getString("parent_id");
		createdAt =  obj.getString("created_at");
		updatedAt =  obj.getString("updated_at");
		countryId =  obj.getString("country_id");
	}

	
}