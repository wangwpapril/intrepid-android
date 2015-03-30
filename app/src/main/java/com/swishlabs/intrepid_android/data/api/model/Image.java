package com.intrepid.travel.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Image implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4382929655913164547L;
	
	public String id;

	public String pid;

	public String sourceUrl;

	public String localeCode;
	
	public String type;
	
	public String width;
	
	public String height;
	
	public String fileSize;
	
	public imageVersion version1;
	
	public imageVersion version2;
	
	public imageVersion version3;
	
	
	public Image(JSONObject obj) throws JSONException {
//		id = obj.getString("id");
		pid = obj.getString("pid");
		sourceUrl = obj.getString("source_url");
		localeCode = obj.getString("locale_code");
		type = obj.getString("type");
		width = obj.getString("width");
		height = obj.getString("height");
		fileSize = obj.getString("file_size");
		
		
		if (obj.has("versions")) {
			
			JSONObject ve = obj.getJSONObject("versions");
			if (ve.has("1x")) {
				version1 = new imageVersion (ve.getJSONObject("1x"));
			}
			
			if (ve.has("2x")) {
				version2 = new imageVersion (ve.getJSONObject("2x"));
			}
			
			if (ve.has("3x")) {
				version3 = new imageVersion (ve.getJSONObject("3x"));
			}
		}

	}
		
}