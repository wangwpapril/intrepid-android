package com.intrepid.travel;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;

import com.intrepid.travel.Enums.NetStatus;
import com.intrepid.travel.Enums.PreferenceKeys;
import com.intrepid.travel.utils.DeviceInfoHelper;
import com.intrepid.travel.utils.Logger;
import com.intrepid.travel.utils.SharedPreferenceUtil;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;


public class MyApplication extends Application implements UncaughtExceptionHandler{
    
	public static String TAG="Travel Smart Application";
//	public User currentUserInfo = null;
//	private UserService userService = null;
	private static DeviceInfoHelper deviceInfoHelper =null;
	
	private ArrayList<Activity> activityList;				
	
	private static boolean loginStatus = false;		
	private static NetStatus netStatus = null;									
	private static String userId;								
	public static Object mLock;
	

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
	
	public static NetStatus getNetStatus() {
		return netStatus;
	}

	public static void setNetStatus(NetStatus netStatus) {
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
		loginStatus = SharedPreferenceUtil.getBoolean(getApplicationContext(),PreferenceKeys.loginStatus.toString(), false);
		activityList = new ArrayList<Activity>();
		
		Thread.setDefaultUncaughtExceptionHandler(this);  
			
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