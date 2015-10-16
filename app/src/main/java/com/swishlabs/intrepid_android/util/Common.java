package com.swishlabs.intrepid_android.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

public class Common {

	public static ProgressDialog dialogLoading;
	public static Context context;
	public static void showHintDialog(String title, String content) {
		if (context != null) {
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
			dialogBuilder
					.setTitle(title)
					.setMessage(content)
					.setCancelable(true)
					.setNegativeButton("Confirmed",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).show();
		}
	}

	public static void showLoading() {
		if (dialogLoading != null)
			return;
		if (context == null)
			return;

		dialogLoading = new ProgressDialog(context);
		dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialogLoading.setTitle("Please wait");
		dialogLoading.setMessage("Reading...");
		dialogLoading.setIndeterminate(false);
		dialogLoading.setCancelable(true);
		dialogLoading.setOnDismissListener(listenerDismissLoading);
		dialogLoading.show();
	}

	public static void showLoading(String str) {
		if (dialogLoading != null)
			return;
		if (context == null)
			return;

		dialogLoading = new ProgressDialog(context);
		dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialogLoading.setMessage(str);
		dialogLoading.setIndeterminate(false);
		dialogLoading.setCancelable(true);
		dialogLoading.setOnDismissListener(listenerDismissLoading);
		dialogLoading.show();
	}

	static OnDismissListener listenerDismissLoading = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {

		}
	};

	public static void cancelLoading() {
		if (context == null)
			return;
		if (dialogLoading != null) {
			if (dialogLoading.isShowing())
				dialogLoading.cancel();
			dialogLoading = null;
		}
	}

	public static View.OnClickListener setupAnalyticsClickListener(final Context context, final String action, final String category, final String label, final int value){
		View.OnClickListener listener;
		if (label!=null && value!=-1) {
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Analytics.with(context).track(action, new Properties().putValue("category", category).putValue("label", label).putValue("value", value));
				}
			};
		}else if (label!=null && value== -1){
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Analytics.with(context).track(action, new Properties().putValue("category", category).putValue("label", label));
				}
			};
		}else if (label==null && value!= -1){
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Analytics.with(context).track(action, new Properties().putValue("category", category).putValue("value", value));
				}
			};
		}else{
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Analytics.with(context).track(action, new Properties().putValue("category", category));
				}
			};
		}
		return listener;
	}

	public static void sendDirectTracking(final Context context, final String action, final String category, final String label, final int value){
		if (label!=null && value!=-1){
			Analytics.with(context).track(action, new Properties().putValue("category", category).putValue("label", label).putValue("value", value));
		}else if (label!=null && value== -1) {
			Analytics.with(context).track(action, new Properties().putValue("category", category).putValue("label", label));
		}else if (label==null && value!= -1){
			Analytics.with(context).track(action, new Properties().putValue("category", category).putValue("value", value));
		}else{
			Analytics.with(context).track(action, new Properties().putValue("category", category));
		}
	}
}