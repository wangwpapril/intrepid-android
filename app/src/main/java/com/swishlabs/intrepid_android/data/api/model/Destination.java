package com.intrepid.travel.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Destination implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8309984110697352775L;
	
	public String id;

	public String name;

	public String type;

	public Image imageCurrency;
	
	public Image imageFlag;
		
	public Destination(JSONObject obj) throws JSONException {
		id = obj.getString("id");
		name = obj.getString("name");
		type = obj.getString("type");
		
		if (obj.has("images")) {
			JSONObject images = obj.getJSONObject("images");
			if (images.has("currency"))
			{
				JSONObject imagecy = images.getJSONObject("currency");
				imageCurrency = new Image(imagecy);
			}
			
			if (images.has("flag"))
			{
				JSONObject imagefg = images.getJSONObject("flag");
				imageFlag = new Image(imagefg);
			}

		}
		
	}

}