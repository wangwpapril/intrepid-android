package com.swishlabs.intrepid_android.util;

import com.swishlabs.intrepid_android.MyApplication;

import android.widget.Toast;

public class ToastHelper {
	
	private static Toast toast;

	public static void showToast(String message,int toastLength) {
		if(message.equals("")){
			if(toast!=null){
				toast.cancel();
			}
			return;
		}
		if(toast == null){
			toast = Toast.makeText(MyApplication.getInstance(), message, toastLength);
		}else{
			toast.setText(message);
		}
		toast.show();
	}
	
	public static String getStringFromResources(int resId){
		return MyApplication.getInstance().getString(resId);
	}
}
