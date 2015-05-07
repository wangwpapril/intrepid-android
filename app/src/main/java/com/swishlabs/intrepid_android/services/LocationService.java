package com.swishlabs.intrepid_android.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;
import com.swishlabs.intrepid_android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class LocationService extends Service {
    private static final int MESSAGE_AUTO_SEND_LOCATION = 1;
    public static final String ACTION_REPORT_POSITION = "Report Position";
    private static final long INTERVAL = 2700000;

    private Timer mTimer = new Timer();
    private TimerTask mTimerTask ;
    private static String userId;
    private static String token;


    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand,startId=", String.valueOf(startId));

        if(token == null)
            token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);
        if(userId == null)
            userId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null);

        if(token == null || userId == null)
            return super.onStartCommand(intent,flags,startId);

        if (intent != null) {
            String action = intent.getAction();

            if(action != null) {
                Log.e("action=", action);
                if(ACTION_REPORT_POSITION.equals(action)){
                    final Location mLocation = getCurrentLocation();
                    if(mLocation != null){
                        sendCoordinatesToIntrepid(mLocation.getLongitude(), mLocation.getLatitude());
                    }

                }
            }
        }
//        initTimer();

        return super.onStartCommand(intent, flags, startId);

    }

    private void initTimer() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = MESSAGE_AUTO_SEND_LOCATION;
                handler.sendMessage(message);

            }
        };

        mTimer.schedule(mTimerTask, 2000, INTERVAL);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_AUTO_SEND_LOCATION) {

                Log.e("Timer is running:", "continue");

                final Location mLocation = getCurrentLocation();
                if(mLocation != null){
                    sendCoordinatesToIntrepid(mLocation.getLongitude(), mLocation.getLatitude());
               }
                super.handleMessage(msg);
            }
        }
    };

    private Location getCurrentLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        //The following code gets the next best location if the current location is unavailable through standard API
        if (location == null) {
            Criteria criteriaTest = new Criteria();
            criteriaTest.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = locationManager.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            location = locationManager.getLastKnownLocation(provider);

            if(location == null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }

         return location;
    }

    private void sendCoordinatesToIntrepid(double longitude, double latitude){

        IControllerContentCallback icc = new IControllerContentCallback() {
            public void handleSuccess(String content){

                JSONObject jsonObj = null;
                Log.d("Location", content);

                try {
                    jsonObj = new JSONObject(content);
                    if(jsonObj.has("error")) {
                        return;
                    }else if(jsonObj.has("coordinate")) {
                        //success
                        Log.e("Location sending:", "Success!");
                        return;
                    }else {

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){
                return;

            }
        };

        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"users/"+userId+"/coordinates?token="+token, icc,
                Enums.ConnMethod.POST,false);

        JSONObject coordinatesDetails = new JSONObject();
        String country = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try
        {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
            {
                country = addresses.get(0).getCountryName();
                cityName = addresses.get(0).getLocality();
                // you should also try with addresses.get(0).toSring();
                if(cityName == null){
                    cityName = addresses.get(0).getSubLocality();
                }

                System.out.println(cityName);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if(cityName == null){
            cityName = "Not Found";
        }

        if(country == null){
            country = "Not Found";
        }

        try {
            coordinatesDetails.put("city", cityName);
            coordinatesDetails.put("country", country);
            coordinatesDetails.put("latitude", String.valueOf(latitude));
            coordinatesDetails.put("longitude", String.valueOf(longitude));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONObject coordinate = new JSONObject();
        try {
            coordinate.put("coordinate", coordinatesDetails);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        cct.execute(coordinate.toString());


    }

}
