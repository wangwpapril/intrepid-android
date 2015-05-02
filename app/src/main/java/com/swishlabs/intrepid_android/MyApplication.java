package com.swishlabs.intrepid_android;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.swishlabs.intrepid_android.activity.SplashActivity;
import com.swishlabs.intrepid_android.data.ServiceManager;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.DeviceInfoHelper;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.Logger;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;


public class MyApplication extends Application implements UncaughtExceptionHandler{
    
	public static String TAG="Travel Smart Application";
//	public User currentUserInfo = null;
//	private UserService userService = null;
	private static DeviceInfoHelper deviceInfoHelper =null;
	
	private ArrayList<Activity> activityList;				
	
	private static boolean loginStatus = false;		
	private static Enums.NetStatus netStatus = null;
	private static String userId;								
	public static Object mLock;

    public DatabaseManager mDatabaseManager;
    public Database mDatabase;

	private static MyApplication instance;
	
	public static MyApplication getInstance(){
		return instance;
	}

	public void exit(){
		for(Activity activity : activityList){
			try {
				activity.finish();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		
//		Intent locationIntent = new Intent(this, LocationService.class);
	//	stopService(locationIntent);
				
		System.exit(0);		
	}

    public void logout(final Activity activity){
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.userId.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.token.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.email.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.firstname.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.lastname.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.username.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.countryCode.toString(), "");
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.currencyCode.toString(), "");
        SharedPreferenceUtil.setBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), false);
        MyApplication.setLoginStatus(false);
        activity.finish();
        Intent mIntent = new Intent(activity, SplashActivity.class);
        startActivity(mIntent);
    }
	
	public DeviceInfoHelper getDeviceInfoHelper(){
		if(deviceInfoHelper == null)
			deviceInfoHelper = new DeviceInfoHelper();
		return deviceInfoHelper;
	}


	
	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		MyApplication.userId = userId;
	}
	
	public static Enums.NetStatus getNetStatus() {
		return netStatus;
	}

	public static void setNetStatus(Enums.NetStatus netStatus) {
		MyApplication.netStatus = netStatus;
	}
	
	public static boolean getLoginStatus() {
		return loginStatus;
	}

	public static void setLoginStatus(boolean loginStatus) {
		MyApplication.loginStatus = loginStatus;
	}
	
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	public ArrayList<Activity> getActivities(){
		refreshActivities();
		return activityList;
	}
	public void refreshActivities(){
		for(int i=0;i<activityList.size();i++){
			if(activityList.get(i).isFinishing())
				activityList.remove(i);
		}
	}

	HashMap<String, Object> intentHashMap;

	public HashMap<String, Object> getIntentHashMap() {
		if(intentHashMap==null){
			intentHashMap=new HashMap<String, Object>();
		}
		return intentHashMap;
	}

	public void setIntentHashMap(HashMap<String, Object> intentHashMap) {
		this.intentHashMap = intentHashMap;
	}
	

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Application onCreate()");
		instance=this;
		mLock=new Object();
		ServiceManager.init(this);
		intentHashMap=new HashMap<String, Object>();
		loginStatus = SharedPreferenceUtil.getBoolean(getApplicationContext(), Enums.PreferenceKeys.loginStatus.toString(), false);
		activityList = new ArrayList<Activity>();
		
		Thread.setDefaultUncaughtExceptionHandler(this);
	    loadDatabase();
	}

    public void loadDatabase(){
        mDatabaseManager = new DatabaseManager();
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
    }

	
	public int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
	}
	
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.e(ex);
		exit();
        android.os.Process.killProcess(android.os.Process.myPid());  
        System.exit(1);	
		
	}
}