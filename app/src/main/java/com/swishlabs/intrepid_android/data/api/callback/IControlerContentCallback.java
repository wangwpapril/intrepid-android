package com.intrepid.travel.net;

public interface IControlerContentCallback {

	public void handleSuccess(String content);

	public void handleError(Exception e);
}
