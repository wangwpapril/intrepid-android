package com.swishlabs.intrepid_android.data.api.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class imageVersion implements Serializable {

	private static final long serialVersionUID = 8457538445307414525L;
	public String pid;
	public String sourceUrl;
	public String localeCode;
	public String type;
	public String width;
	public String height;
	public String fileSize;
	public imageVersion(JSONObject obj) throws JSONException {

        if(obj == null) {
            return;
        }
		pid = obj.getString("pid");
		sourceUrl = obj.getString("source_url");
		localeCode = obj.getString("locale_code");
		type = obj.getString("type");
		width = obj.getString("width");
		height = obj.getString("height");
		fileSize = obj.getString("file_size");
		
	}
	
}