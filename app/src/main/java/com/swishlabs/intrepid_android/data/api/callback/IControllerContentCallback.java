package com.swishlabs.intrepid_android.data.api.callback;

public interface IControllerContentCallback {
	public void handleSuccess(String content);
	public void handleError(Exception e);
}
