package com.swishlabs.intrepid_android.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ryanracioppo on 15-05-06.
 */
public class UpdateLocationService extends AsyncTask<String, String, Boolean> {

    Context mContext;

    public UpdateLocationService(AndroidLocationServices context) {
        mContext = (Context)context;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if (!SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null).isEmpty()){
            sendCoordinatesToIntrepid(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
        }else {
            return null;
        }
        return true;
    }

    private void sendCoordinatesToIntrepid(double longitude, double latitude){

        IControllerContentCallback icc = new IControllerContentCallback() {
            public void handleSuccess(String content){

                JSONObject jsonObj = null, coordObj = null;
                User user = null;
                Log.d("signUp user", content);

                try {
                    jsonObj = new JSONObject(content);
                    if(jsonObj.has("error")) {
                        JSONArray errorMessage = jsonObj.getJSONObject("error").getJSONArray("message");
                        String message = String.valueOf((Object) errorMessage.get(0));
                        Log.d("UpdateLocationService", message);

                    }else if(jsonObj.has("coordinate")) {
                        //success
                        return;
                    }else {
//                            StringUtil.showAlertDialog("Error", "Could not send your coordinates to Intrepid API", AssistanceActivity.this);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){
//                    StringUtil.showAlertDialog("Error", "Could not send your coordinates to Intrepid API", AssistanceActivity.this);

                return;

            }
        };
        String token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);
        String userId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null);
        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"users/"+userId+"/coordinates?token="+token, icc,
                Enums.ConnMethod.POST,false);

        JSONObject coordinatesDetails = new JSONObject();
        String country = mContext.getResources().getConfiguration().locale.getDisplayCountry();
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        try
        {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
            {
                cityName = addresses.get(0).getLocality();
                // you should also try with addresses.get(0).toSring();
                System.out.println(cityName);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
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