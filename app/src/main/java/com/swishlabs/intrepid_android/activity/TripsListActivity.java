package com.swishlabs.intrepid_android.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.TripsListAdapter;
import com.swishlabs.intrepid_android.data.api.callback.ControlerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControlerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.Destination;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TripsListActivity extends BaseActivity {

	protected static final String TAG = "TripsListActivity";
	private ListView listView;
	private List<Destination> mDestinationList;
	private TripsListAdapter tripsListAdapter;



	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.trip_list);
		initView();
		
		if(mDestinationList == null) {
			getTripList();
		}else{
			tripsListAdapter = new TripsListAdapter(
                    mDestinationList, context);
			listView.setAdapter(tripsListAdapter);
		}
	
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
                CreateTrip(position);
			}
		});
	}

    public int getTripCount() {
        String countQuery = "SELECT  * FROM " + Database.TABLE_TRIPS;
        Cursor cursor = mDatabase.getDb().rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public boolean isTripUnique(String destinationName){
        
    }

    private void CreateTrip(int position){
        Destination destination = mDestinationList.get(position);
//        Trip trip = new Trip(destination.getCountry());


        ContentValues values = new ContentValues();
        values.put(Database.KEY_ID, getTripCount());
        values.put(Database.KEY_DESTINATION_COUNTRY, destination.getCountry());


        // Inserting Row
        mDatabase.getDb().insert(Database.TABLE_TRIPS, null, values);
        Log.d("Trip List", "You inserted some values into tthe TABLE :O");
//        db.close(); // Closing database connection
        Trip trip = getTrip(0);
        Log.d("Trip List", "The first trip is to:"+trip.getDestinationName());
    }

    public Trip getTrip(int id) {
        Cursor cursor = mDatabase.getDb().query(Database.TABLE_TRIPS, new String[] { Database.KEY_ID,
                        Database.KEY_DESTINATION_COUNTRY}, Database.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Trip trip = new Trip(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return contact
        return trip;
    }
	
	private void getTripList(){

		IControlerContentCallback icc = new IControlerContentCallback() {
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
					
					tripsListAdapter = new TripsListAdapter(
                            mDestinationList, context);
					listView.setAdapter(tripsListAdapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			public void handleError(Exception e){

				return;

			}
		};
		
		String token = null;
		token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);
		
		ControlerContentTask cct = new ControlerContentTask(
                Constants.BASE_URL+"destinations?short_list=true&token=" + token, icc,
				Enums.ConnMethod.GET,false);
		String ss = null;
		cct.execute(ss);

	}

	@Override
	protected void initTitle(){
		ivTitleBack.setVisibility(View.VISIBLE);
		ivTitleBack.setOnClickListener(this);
		tvTitleName.setText("Trips");
//		ivTitleRight.setVisibility(View.VISIBLE);
//		ivTitleBack.setImageResource(R.drawable.title_left_search);
	}

	@Override
	public void onClick(View v){
		if(v == ivTitleBack){
			finish();
		}else if(v == ivTitleRight){
			
		}
	}




}
