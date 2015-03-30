package com.swishlabs.intrepid_android.data.api.callback;

public interface IControlerContentCallback {

	public void handleSuccess(String content);

	public void handleError(Exception e);
}
