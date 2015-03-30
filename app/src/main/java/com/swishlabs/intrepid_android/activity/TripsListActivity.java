package com.swishlabs.intrepid_android.activity;

import android.os.Bundle;
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
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
