package com.swishlabs.intrepid_android.data.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Destination implements Serializable {

	private static final long serialVersionUID = 8309984110697352775L;
    public int index = 0;
	public String id;
	public String name;
    public String altName;
	public String type;
    public String generalImage;
    public String currencyCode;
	public Image imageCurrency;
	public Image imageFlag;
		
	public Destination(JSONObject obj) throws JSONException {

        if(obj == null)
            return;

		id = obj.getString("id");
		name = obj.getString("name");
        altName = obj.optString("alt_name");
		type = obj.getString("type");
		
		if (obj.has("images")) {
			JSONObject images = obj.getJSONObject("images");
			if (images.has("currency"))
			{
                Object rr = images.get("currency");
                if(rr instanceof JSONObject) {
                    JSONObject imagecy = images.getJSONObject("currency");
                    imageCurrency = new Image(imagecy);
                } else {
                    imageCurrency = new Image(null);
                }
			}
			
			if (images.has("flag"))
			{
                Object rr = images.get("flag");
                if(rr instanceof JSONObject) {
                    JSONObject imagefg = images.getJSONObject("flag");
                    imageFlag = new Image(imagefg);
                } else {
                    imageFlag = new Image(null);
                }
			}

		}
		
	}

    public String getCountry(){
        return name;
    }

    public String getId(){
        return id;
    }

    public int getIndex() { return index; }

    public String getGeneralImage(){
        return generalImage;
    }

    public String getCurrencyCode() { return currencyCode; }

}