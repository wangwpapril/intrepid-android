package com.swishlabs.intrepid_android.data.api.callback;

import org.json.JSONException;

public interface IControllerContentCallback {
	public void handleSuccess(String content) throws JSONException;
	public void handleError(Exception e);
}
