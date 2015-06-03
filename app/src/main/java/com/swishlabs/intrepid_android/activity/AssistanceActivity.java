package com.swishlabs.intrepid_android.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.segment.analytics.Analytics;
import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.customViews.IntrepidMenu;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.AssistanceProvider;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.util.Common;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;
import com.swishlabs.intrepid_android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AssistanceActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    IntrepidMenu mIntrepidMenu;
    List<AssistanceProvider> mApList;
    String[] mApNameArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Assi", "onCreate");
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_assistance);
        network();
        if (mNetworkConnectivity) {
            buildGoogleApiClient();
        }else{
            connectionFailAlert();
            setUpMapIfNeeded();
        }
        mIntrepidMenu = (IntrepidMenu)findViewById(R.id.intrepidMenu);
        mIntrepidMenu.setupMenu(this, AssistanceActivity.this, true);
        mApList = SharedPreferenceUtil.getApList(this);
        setupCallAssistanceButton();
        TextView instructionalTextView = (TextView)findViewById(R.id.instructionalText);
        String instructions = SharedPreferenceUtil.getString(Enums.PreferenceKeys.instructionalText.toString(), null);
        if (instructions!=null) {
            instructions = instructions.replace("1.", "\n1.");
            instructions = instructions.replace("2.", "\n2.");
            instructionalTextView.setText(instructions);
        }

    }

    private ConnectivityManager mConnectivityManager;
    private boolean mNetworkConnectivity;

    public void network(){
        mConnectivityManager =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        mNetworkConnectivity = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }




    @Override
    protected void onResume(){
        super.onResume();
        Analytics.with(this).screen(null, "Assistance");
    }

    GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        setUpMapIfNeeded();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        connectionFailAlert();
        setUpMapIfNeeded();
    }

    private void connectionFailAlert(){
        StringUtil.showAlertDialog("Location Services Failed", "Could not access current location. Using last known location", AssistanceActivity.this);
    }

    private void setupCallAssistanceButton(){

        mApNameArray = new String[mApList.size()];
        for (int i = 0;i<mApList.size(); i++){
            mApNameArray[i] = mApList.get(i).getName() +": "+ mApList.get(i).getPhone();
        }

        Button assistanceButton = (Button)findViewById(R.id.callIntrepidAssistance);
        assistanceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssistanceActivity.this);
                builder.setItems(mApNameArray, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AssistanceProvider apSelected = mApList.get(which);
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:" + apSelected.getPhone()));
                    Common.sendDirectTracking(AssistanceActivity.this, "Call Assistance", "Assistance", apSelected.getPhone(), -1);
                    startActivity(call);
                }
            };

    private void setUpMapIfNeeded() {
        Log.d("Assi","setUpMapIfNeeded");
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }else{
                StringUtil.showAlertDialog("Could Not Load Map", "Please install Google Maps on your phone.", AssistanceActivity.this);
            }
        }else {
 //           setUpMap();
        }
    }

    private void setUpMap() {
        Log.d("Assi","setUpMap");

        Location currentLocation = getCurrentLocation();
        if (currentLocation != null)
        {
            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 9));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }else{
            StringUtil.showAlertDialog("Error", "Could not retrieve your coordinates", AssistanceActivity.this);
        }

    }

    private Location getCurrentLocation(){
        Log.d("Assi","getCurrentLocation");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location;
        if (mGoogleApiClient!=null) {
            location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }else{
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        }
        //The following code gets the next best location if the current location is unavaiable through standard API
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

        if (location!=null) {
            sendCoordinatesToIntrepid(location.getLongitude(), location.getLatitude());
        }
        return location;
    }

    private void sendCoordinatesToIntrepid(double longitude, double latitude){
        Log.d("Assi","sendCoord");

            IControllerContentCallback icc = new IControllerContentCallback() {
                public void handleSuccess(String content){

                    JSONObject jsonObj = null;
                    Log.d("Assistance", content);

                    try {
                        jsonObj = new JSONObject(content);
                        if(jsonObj.has("error")) {
                            JSONArray errorMessage = jsonObj.getJSONObject("error").getJSONArray("message");
                            String message = String.valueOf((Object) errorMessage.get(0));
                            StringUtil.showAlertDialog("Error", message, AssistanceActivity.this);

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
        String country = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try
        {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
            {
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

    @Override
    public void onBackPressed(){
        if(mIntrepidMenu.mState == 1){
            mIntrepidMenu.snapToBottom();
            return;
        }
        super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }



}
