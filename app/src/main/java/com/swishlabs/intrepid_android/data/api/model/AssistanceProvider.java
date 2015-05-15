package com.swishlabs.intrepid_android.data.api.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class AssistanceProvider implements Serializable {

	private static final long serialVersionUID = -6491498174329215221L;
	public String id;

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String name;
	public String phone;
	public AssistanceProvider(JSONObject obj) throws JSONException {
        if(obj == null)
            return;

		id = obj.getString("id");
		name = obj.getString("name");
		phone =  obj.getString("phone");
	}
}