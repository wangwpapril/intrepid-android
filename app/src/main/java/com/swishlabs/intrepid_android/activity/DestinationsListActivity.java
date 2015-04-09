package com.swishlabs.intrepid_android.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.DestinationsListAdapter;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.Destination;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;
import com.swishlabs.intrepid_android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DestinationsListActivity extends BaseActivity {

	protected static final String TAG = "TripsListActivity";
	private ListView listView;
	private List<Destination> mDestinationList;
	private DestinationsListAdapter mDestinationsListAdapter;
    public static EditText mEditTextSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.destination_list);
		initView();
		
		if(mDestinationList == null) {
			getTripList();
		}else{
			mDestinationsListAdapter = new DestinationsListAdapter(
                    mDestinationList, context);
			listView.setAdapter(mDestinationsListAdapter);
		}
	
	}

    private void getTripList(){

        IControllerContentCallback icc = new IControllerContentCallback() {
            public void handleSuccess(String content){

                JSONObject des;
                try {
                    des = new JSONObject(content);
                    JSONArray array = des.getJSONArray("destinations");
                    int len = array.length();
                    mDestinationList = new ArrayList<Destination>(len);
                    for (int i =0;i < len; i++){
                        mDestinationList.add(new Destination(array.getJSONObject(i)));
                    }

                    mDestinationsListAdapter = new DestinationsListAdapter(
                            mDestinationList, context);
                    listView.setAdapter(mDestinationsListAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){

                StringUtil.showAlertDialog("Trips", "Data error !", context);
                return;

            }
        };

        String token = null;
        token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);

        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"destinations?short_list=true&token=" + token, icc,
                Enums.ConnMethod.GET,false);
        String ss = null;
        cct.execute(ss);

    }

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initView(){
		super.initTitleView();
		listView = (ListView) findViewById(R.id.trip_list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
                Log.d("Trip list", "You hit destination:" + position);
                LoadTripFromApi(position);

			}
		});

        mEditTextSearch = (EditText) findViewById(R.id.search_ed);
        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mEditTextSearch.getText().toString().toLowerCase(Locale.getDefault());
                mDestinationsListAdapter.getFilter(context).filter(text);

            }
        });

    }

    public void LoadTripFromApi(final int destinationPosition){
        final String destinationId = mDestinationList.get(destinationPosition).getId();

        IControllerContentCallback icc = new IControllerContentCallback() {

            public void handleSuccess(String content){

                JSONObject destination;
                try {
                    destination = new JSONObject(content).getJSONObject("destination");

                    JSONObject images = destination.getJSONObject("images");
                    final String general_image_url = images.getJSONObject("intro").getString("source_url");
//                    Thread t = new Thread(new Runnable() {
//                        public void run() {
//                            Looper.prepare();
//                            String generalImageUri = SaveImage.saveImageLocally(general_image_url, "tripImage"+destinationId, DestinationsListActivity.this);
//                            CreateTrip(destinationPosition, generalImageUri);
//                        }
//                    });
//
//                    t.start();
                    String encodedURL = general_image_url.replace(" ", "%20");
                            CreateTrip(destinationPosition, encodedURL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){

            }
        };

        String token = null;
        token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);
        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"destinations/"+destinationId+"?token=" + token, icc,
                Enums.ConnMethod.GET,false);
        String ss = null;
        cct.execute(ss);
    }

    public boolean isTripUnique(String destinationName){
        return true;
    }

    private void CreateTrip(int position, String generalImageUri){
        Destination destination = mDestinationList.get(position);
//        Trip trip = new Trip(destination.getCountry());


        ContentValues values = new ContentValues();
        values.put(Database.KEY_DESTINATION_COUNTRY, destination.getCountry());
        values.put(Database.KEY_COUNTRY_ID, destination.getId());
        values.put(Database.KEY_GENERAL_IMAGE_URI, generalImageUri);


        // Inserting Row
        mDatabase.getDb().insert(Database.TABLE_TRIPS, null, values);
//        db.close(); // Closing database connection
//        Trip trip = getTrip(0);
//        Log.d("Trip List", "The first trip is to:"+trip.getDestinationName());

        Intent intent = new Intent(DestinationsListActivity.this, TripPagesActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public Trip getTrip(int id) {
        Cursor cursor = mDatabase.getDb().query(Database.TABLE_TRIPS, new String[]{Database.KEY_ID,
                        Database.KEY_DESTINATION_COUNTRY, Database.KEY_COUNTRY_ID, Database.KEY_GENERAL_IMAGE_URI}, Database.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Trip trip = new Trip(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return trip;
    }
	


	@Override
	protected void initTitle(){
	}

	@Override
	public void onClick(View v){
	}




}
