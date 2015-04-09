package com.swishlabs.intrepid_android.util;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.swishlabs.intrepid_android.MyApplication;

public class DeviceInfoHelper {

	private TelephonyManager mTelephonyManager;
	private Context mContext;
	private static String ANDROID_DEFAULT_DEVICE_ID ="Android.IMEI.2015";
	private static final int ERROR = -1;

	public DeviceInfoHelper() {
		this.mTelephonyManager = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		this.mContext = MyApplication.getInstance();
	}

	public String getPhoneNumberString(){
		return mTelephonyManager.getLine1Number()==null?"":mTelephonyManager.getLine1Number();
	}

	public String getDeviceId(){
	 
		String phoneDeviceID = mTelephonyManager.getDeviceId();
		String padDeviceID = Secure.getString(this.mContext.getContentResolver(), Secure.ANDROID_ID);
		if( phoneDeviceID != null){
			return phoneDeviceID;
		}else if(padDeviceID!= null ){
			return padDeviceID;
		}else{
			return ANDROID_DEFAULT_DEVICE_ID;
		}		
	}

	public String translateDeviceId(){
		String deviceId=getDeviceId();
		if(deviceId.length()>16){
			deviceId=Encrypt.toMD5(deviceId).substring(5, 20);
		}
		return deviceId;
		
	}

	public String getDeviceSoftwareVersion(){
		return mTelephonyManager.getDeviceSoftwareVersion();
	}

	public String getDisplayMetrics(){
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.getDefaultDisplay().getMetrics(dm);  
		
		return dm.heightPixels + "x" + dm.widthPixels;   
	}

	public String getDeviceModel(){
		return  android.os.Build.MODEL;
	}

	public static String getDeviceVersionSDK(){
		return  android.os.Build.VERSION.SDK;
	} 

	public static String getDeviceVersionRelease(){
		return  android.os.Build.VERSION.RELEASE;
	} 	

	public static String[] getAppVersionName(Context context) {     
	   String version[] = new String[2];
	   try {     
	       // ---get the package info---     
	       PackageManager pm = context.getPackageManager();     
	       PackageInfo pi = pm.getPackageInfo(context.getPackageName(),0);     
	       version[0] = String.valueOf(pi.versionCode);
	       version[1] = pi.versionName;   
	              
	    } catch (Exception e) {     
	        Log.e("VersionInfo", "Exception", e); 
	        Logger.e(e);
	    }  
	   return version;
	} 
	

	public Enums.NetStatus getNetStatus(){
		ConnectivityManager connMgr = null;
		NetworkInfo activeInfo = null;
		
		try {
			connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			activeInfo = connMgr.getActiveNetworkInfo(); 			
		} catch (Exception e) {
			Logger.e(e);
		}
		
		if (activeInfo != null && activeInfo.isConnected()) {	
			switch (activeInfo.getType()) {
			case ConnectivityManager.TYPE_MOBILE:			
				return Enums.NetStatus.MOBILE;
			case ConnectivityManager.TYPE_WIFI:				
				return Enums.NetStatus.WIFI;
			default:	
				return Enums.NetStatus.Disable;
			}
		}
		else{	
			return Enums.NetStatus.Disable;
		}
	}

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}