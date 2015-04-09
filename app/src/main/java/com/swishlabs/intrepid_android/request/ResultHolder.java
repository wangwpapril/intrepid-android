package com.swishlabs.intrepid_android.request;

public class ResultHolder {
	private boolean isSuccess;
	private String result;
	public boolean isSuccess(){
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess){
		this.isSuccess = isSuccess;
	}
	public String getResult(){
		return result;
	}
	public void setResult(String result){
		this.result = result;
	}
}
