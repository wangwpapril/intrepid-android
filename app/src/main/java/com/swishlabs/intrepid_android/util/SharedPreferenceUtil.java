package com.swishlabs.intrepid_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.data.api.model.AssistanceProvider;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferenceUtil {
	private static final String SHARED_PREFERENCE_NAME = "travel smart";

	public static void setString(String key, String value) {
		SharedPreferences sharedPreferences = MyApplication.getInstance().getApplicationContext().getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void setInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String getString(String key,
			String defaultValue) {
		SharedPreferences sharedPreferences = MyApplication.getInstance().getApplicationContext().getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}

	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static boolean getBoolean(Context context, String key,
			boolean defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static void setApList(Context context, List<AssistanceProvider> apList) {
		SharedPreferences appSharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEditor = appSharedPrefs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(apList);
		prefsEditor.putString("AssistanceList", json);
		prefsEditor.commit();
	}

	public static List<AssistanceProvider> getApList(Context context){
		SharedPreferences appSharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEditor = appSharedPrefs.edit();
		Gson gson = new Gson();
		String json = appSharedPrefs.getString("AssistanceList", "");
		Type type = new TypeToken<List<AssistanceProvider>>(){}.getType();
		List<AssistanceProvider> apList = gson.fromJson(json, type);

		return apList;
	}


}