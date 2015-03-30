package com.intrepid.travel.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7442295700648642515L;
	
	public String id;
	public String name;
	public String email;
	public String groupNumber;
	
	public Parent parent;
	
	public String classes;
	public String countryCode;
	
	public List<AssistanceProvider> apList;
	
	public String content;
	public String images;
	public String localeCode;
	public String createdAt;
	public String updatedAt;
		
	public Company(String name){
		this.name = name;
	}
	
	public Company(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		name = obj.getString("name");
		email = obj.getString("email");
		groupNumber = obj.getString("group_number");
		classes = obj.getString("class");
		countryCode = obj.getString("country_code");
		content = obj.getString("content");
		images = obj.getString("images");
		localeCode = obj.getString("locale_code");
		createdAt = obj.getString("created_at");
		updatedAt = obj.getString("updated_at");
		
		
		if (obj.has("parent")) {
			JSONObject pa = obj.getJSONObject("parent");
			parent = new Parent(pa);
		}
		
		if (obj.has("assistance_providers")) {
			JSONArray array = obj.getJSONArray("assistance_providers");
			int len = array.length();
			
			if (len > 0) {
				apList = new ArrayList<AssistanceProvider>(len);
				for (int i = 0; i < len; i ++) {
					apList.add(new AssistanceProvider(array.getJSONObject(i)));
				}
			}
		}
	}

	

	
}