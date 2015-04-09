package com.swishlabs.intrepid_android.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Debug;
import android.util.Log;


public class Logger {


	private static final String TAG = "TravelSmart";

	public static void d(String message) {
		Log.d(TAG, message);

	}

	public static void e(Throwable e) {
		Log.e(TAG, e.getMessage(), e);

	}

	
	public static void logHeap(Class clazz) { 
	    Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576)); 
	    Double available = new Double(Debug.getNativeHeapSize())/1048576.0; 
	    Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0; 
	    DecimalFormat df = new DecimalFormat(); 
	    df.setMaximumFractionDigits(2); 
	    df.setMinimumFractionDigits(2); 
	 
	    Log.d(TAG, "debug. ================================="); 
	    Log.d(TAG, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free) in [" + clazz.getName().replaceAll("com.myapp.android.","") + "]"); 
	    Log.d(TAG, "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)"); 
	    System.gc(); 
	    System.gc(); 
	} 

	
	private static String buildSystemMessage(Throwable e) {

		String message = null;

		message = String.format("%s,%s,%s,%s",
				e.getStackTrace()[0].getClassName(),
				e.getStackTrace()[0].getMethodName(),
				e.getStackTrace()[0].getLineNumber(), e.toString());

		return message;
	}


	private static void Log(String message, File file) {
		if (file == null)
			return;

		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(file, true);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			fos.write((sdf.format(new Date()) + "," + message).getBytes("gbk"));

			fos.write("\r\n".getBytes("gbk"));

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {

				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {

				e.printStackTrace();

			}
			fos = null;
			file = null;
		}
	}
}