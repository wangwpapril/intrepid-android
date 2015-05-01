package com.swishlabs.intrepid_android.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
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

public class AssistanceActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    IntrepidMenu mIntrepidMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance);
        setUpMapIfNeeded();
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(this, AssistanceActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        Location currentLocation = getCurrentLocation();
        if (currentLocation != null)
        {
            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 9));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }else{
            StringUtil.showAlertDialog("Error", "Could not retrieve your coordinates", AssistanceActivity.this);
        }

    }

    private Location getCurrentLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        //The following code gets the next best location if the current location is unavaiable through standard API
        if (location == null) {
            Criteria criteriaTest = new Criteria();
            criteriaTest.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = locationManager.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            location = locationManager.getLastKnownLocation(provider);
        }
        if (location!=null) {
            sendCoordinatesToIntrepid(location.getLongitude(), location.getLatitude());
        }
        return location;
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
                            StringUtil.showAlertDialog("Error", message, AssistanceActivity.this);

                        }else if(jsonObj.has("coordinates")) {
                            //success
                        }else {
                            StringUtil.showAlertDialog("Error", "Could not send your coordinates to Intrepid API", AssistanceActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                public void handleError(Exception e){
                    StringUtil.showAlertDialog("Error", "Could not send your coordinates to Intrepid API", AssistanceActivity.this);


                }
            };
            String userId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null);
            ControllerContentTask cct = new ControllerContentTask(
                    Constants.BASE_URL+"/users/"+userId+"/coordinates/", icc,
                    Enums.ConnMethod.POST,false);

            JSONObject coordinatesDetails = new JSONObject();
            try {
                coordinatesDetails.put("latitude", latitude);
                coordinatesDetails.put("longitude", longitude);
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
            Log.d("coordinate data",coordinate.toString());

        }
}
