package com.swishlabs.intrepid_android.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.segment.analytics.Analytics;
import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.adapter.DestinationsListAdapter;
import com.swishlabs.intrepid_android.customViews.ClearEditText;
import com.swishlabs.intrepid_android.data.api.callback.ControllerContentTask;
import com.swishlabs.intrepid_android.data.api.callback.IControllerContentCallback;
import com.swishlabs.intrepid_android.data.api.model.Alert;
import com.swishlabs.intrepid_android.data.api.model.Constants;
import com.swishlabs.intrepid_android.data.api.model.Destination;
import com.swishlabs.intrepid_android.data.api.model.HealthCondition;
import com.swishlabs.intrepid_android.data.api.model.HealthMedicationDis;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.data.store.Database;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;
import com.swishlabs.intrepid_android.util.Common;
import com.swishlabs.intrepid_android.util.DataDownloader;
import com.swishlabs.intrepid_android.util.Enums;
import com.swishlabs.intrepid_android.util.SharedPreferenceUtil;
import com.swishlabs.intrepid_android.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DestinationsListActivity extends BaseActivity {

	protected static final String TAG = "TripsListActivity";
	private ListView listView;
	private List<Destination> mDestinationList;
	private DestinationsListAdapter mDestinationsListAdapter;
    public static ClearEditText mEditTextSearch;
    private int mCallbackCount = 0;

    private List<HealthCondition> healthConditionList;
    private List<HealthMedicationDis> healthMedicationList;
    private List<Alert> mAlertList;



    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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
                    DatabaseManager.deleteCurrency(mDatabase);

                    for (int i =0;i < len; i++){
                        Destination dest = new Destination(array.getJSONObject(i));
                        dest.index = i;
                        mDestinationList.add(dest);
                        String currencyCode = null;
//                        String currencyUrl = mDestinationList.get(i).imageCurrency.sourceUrl.replace(" ", "%20");
                        String currencyUrl = mDestinationList.get(i).imageCurrency.version3.sourceUrl;
                        if(currencyUrl != null) {
                            String[] parts = currencyUrl.replace(" ", "%20").split("/");
                            for (int j = 0; j < parts.length; j++) {
                                if (parts[j].equals("currency")) {
                                    currencyCode = parts[j + 1];
                                    break;
                                }
                            }
                        }

                        if(currencyCode == null)
                            currencyCode = SharedPreferenceUtil.getString(Enums.PreferenceKeys.currencyCode.toString(), null);

                        mDestinationList.get(i).currencyCode = currencyCode;
                        saveCurrencyImage(currencyCode, currencyUrl);
                    }

                    mDestinationsListAdapter = new DestinationsListAdapter(
                            mDestinationList, context);
                    listView.setAdapter(mDestinationsListAdapter);

                } catch (JSONException e) {
                    StringUtil.showAlertDialog("Trips", "Data error !", context);
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
    protected void onResume(){
        super.onResume();
        Analytics.with(this).screen(null, "Add Trip");
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
                Log.d("Trip list", "You hit destination list index:" + arg3);
//                LoadCurrencyInfo((int) arg3);
//                LoadTripFromApi((int) arg3, null);
                DataDownloader downloader = new DataDownloader();
                downloader.initializeDownload(DestinationsListActivity.this, mDestinationList.get((int) arg3), DestinationsListActivity.this, null, null);


            }
        });


        mEditTextSearch = (ClearEditText) findViewById(R.id.search_ed);
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
                if (text.length() == 0){
                    Common.sendDirectTracking(DestinationsListActivity.this, "Cancel Search", "Add Trip", null, -1);
                }else {
                    Common.sendDirectTracking(DestinationsListActivity.this, "Keyword", "Add Trip", text, -1);
                }

                if (mDestinationsListAdapter != null && !mDestinationsListAdapter.isEmpty()) {
                    mDestinationsListAdapter.getFilter(context).filter(text);
                }


            }
        });
        mEditTextSearch.setOnClickListener(Common.setupAnalyticsClickListener(DestinationsListActivity.this, "Search Field", "Add Trip", null, -1));

    }

    public void redirectToTripOverview(String destinationId){
        Intent intent = new Intent(DestinationsListActivity.this, ViewDestinationActivity.class);
        intent.putExtra("destinationId", destinationId);
        intent.putExtra("firstTimeFlag","1");
        startActivity(intent);
    }



    private void saveCurrencyImage(String code, String url){
        ContentValues values = new ContentValues();
        values.put(Database.KEY_GENERAL_IMAGE_URI, url);
        values.put(Database.KEY_CURRENCY_CODE,code);
        mDatabase.getDb().insert(Database.TABLE_CURRENCY, null, values);
    }



    @Override
	protected void initTitle(){
	}

	@Override
	public void onClick(View v){
	}


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}
