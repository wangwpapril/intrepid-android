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
import com.swishlabs.intrepid_android.data.api.model.HealthCondition;
import com.swishlabs.intrepid_android.data.api.model.HealthMedicationDis;
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

    private List<HealthCondition> healthConditionList;
    private List<HealthMedicationDis> healthMedicationList;


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
                JSONArray healthCondition, healthMedication;
                String countryId;
                try {
                    destination = new JSONObject(content).getJSONObject("destination");
                    JSONObject country = destination.getJSONObject("country");
                    if (country!=null){
                        String countryCode = country.getString("country_code");
                        saveEmbassyInformation(mDestinationList.get(destinationPosition).getId(), countryCode);
                    }
                    countryId = destination.optString("id");
                    SharedPreferenceUtil.setString(Enums.PreferenceKeys.currentCountryId.toString(),countryId);
                    if(destination.has("health_conditions")) {
                        healthCondition = destination.getJSONArray("health_conditions");
                        int len = healthCondition.length();
                        healthConditionList = new ArrayList<HealthCondition>(len);

                        for(int i = 0;i<len;i++){
                            healthConditionList.add(new HealthCondition(healthCondition.getJSONObject(i)));
                            CreateHealthCondition(destination.optString("id"), String.valueOf(i));

                        }

                    }

                    if(destination.has("medications")){
                        healthMedication = destination.getJSONArray("medications");
                        int len = healthMedication.length();
                        healthMedicationList = new ArrayList<HealthMedicationDis>(len);

                        for(int i = 0;i < len;i++){
                            JSONObject temp = healthMedication.getJSONObject(i);
                            HealthMedicationDis tempMed = new HealthMedicationDis();
                            tempMed.id = Integer.valueOf(temp.optString("id"));
                            tempMed.mMedicationName = temp.optString("name");
                            tempMed.mCountryId = countryId;
                            tempMed.mBrandNames = temp.getJSONObject("content").optString("brand_names");
                            tempMed.mDescription = temp.getJSONObject("content").optString("description");
                            tempMed.mSideEffects = temp.getJSONObject("content").optString("side_effects");
                            tempMed.mStorage = temp.getJSONObject("content").optString("storage");
                            tempMed.mNotes = temp.getJSONObject("content").optString("notes");
                            tempMed.mGeneralImage = temp.getJSONObject("images").getJSONObject("general")
                                    .getJSONObject("versions").getJSONObject("3x").optString("source_url").replace(" ", "%20");

                            healthMedicationList.add(tempMed);
                            CreateHealthMedication(countryId, String.valueOf(i));

                        }

                    }

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

                    saveDestinationInformation(destination, images);
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

    private void saveEmbassyInformation(String destinationId, String countryCode){

        IControllerContentCallback icc = new IControllerContentCallback() {

            public void handleSuccess(String content){

                JSONObject diplomaticOffices;
                try {
                    diplomaticOffices = new JSONObject(content);
                    diplomaticOffices.getString("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){

            }
        };

        String token = null;
        token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);
        String currentCountryCode = SharedPreferenceUtil.getString(Enums.PreferenceKeys.countryCode.toString(), null);
        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"diplomatic-offices/"+countryCode+"?origin_country="+currentCountryCode+"&token=" + token, icc,
                Enums.ConnMethod.GET,false);
        String test = Constants.BASE_URL+"diplomatic-offices/"+countryCode+"?origin_country="+currentCountryCode+"&token=" + token;
        String ss = null;
        cct.execute(ss);
    }

    private void saveDestinationInformation(JSONObject destination, JSONObject images) throws JSONException {
        JSONObject content = destination.getJSONObject("content");
        String destinationId = destination.getString("id");
        String communicationsInfrastructure = content.getString("communication_infrastructure");
        String otherConcerns = content.getString("other_concerns");
        String development = content.getString("development");
        String location = content.getString("location");
        String cultural_norms = content.getString("cultural_norms");
        String sources = content.getString("sources");
        String currency = content.getString("currency");
        String religion = content.getString("religion");
        String time_zone = content.getString("time_zone");
        String safety = content.getString("safety");
        String type_of_government = content.getString("type_of_government");
        String visa_map_attributions = content.getString("visa_map_attributions");
        String electricity = content.getString("electricity");
        String ethnic_makeup = content.getString("ethnic_makeup");
        String language = content.getString("language");
        String visa_requirements = content.getString("visa_requirements");
        String climate = content.getString("climate");
        String intro_image_url = images.getJSONObject("intro").getString("source_url").replace(" ", "%20");
        String security_image_url = images.getJSONObject("security").getString("source_url").replace(" ", "%20");
        String overview_image_url = images.getJSONObject("overview").getString("source_url").replace(" ", "%20");
        String culture_image_url = images.getJSONObject("culture").getString("source_url").replace(" ", "%20");
        String currency_image_url = images.getJSONObject("currency").getString("source_url").replace(" ", "%20");

        ContentValues values = new ContentValues();
        values.put(Database.KEY_DESTINATION_ID, destinationId);
        values.put(Database.KEY_COMMUNICATIONS, communicationsInfrastructure);
        values.put(Database.KEY_OTHER_CONCERNS, otherConcerns);
        values.put(Database.KEY_DEVELOPMENT, development);
        values.put(Database.KEY_LOCATION, location);
        values.put(Database.KEY_CULTURAL_NORMS, cultural_norms);
        values.put(Database.KEY_SOURCES, sources);
        values.put(Database.KEY_CURRENCY, currency);
        values.put(Database.KEY_RELIGION, religion);
        values.put(Database.KEY_TIMEZONE, time_zone);
        values.put(Database.KEY_SAFETY, safety);
        values.put(Database.KEY_GOVERNMENT, type_of_government);
        values.put(Database.KEY_VISAMAP, visa_map_attributions);
        values.put(Database.KEY_ELECTRICITY, electricity);
        values.put(Database.KEY_ETHNIC_MAKEUP, ethnic_makeup);
        values.put(Database.KEY_LANGUAGE_INFORMATION, language);
        values.put(Database.KEY_VISA_REQUIREMENT, visa_requirements);
        values.put(Database.KEY_CLIMATE_INFO, climate);
        values.put(Database.KEY_IMAGE_SECURITY, security_image_url);
        values.put(Database.KEY_IMAGE_OVERVIEW, overview_image_url);
        values.put(Database.KEY_IMAGE_CULTURE, culture_image_url);
        values.put(Database.KEY_IMAGE_INTRO, intro_image_url);
        values.put(Database.KEY_IMAGE_CURRENCY, currency_image_url);

        mDatabase.getDb().insert(Database.TABLE_DESTINATION_INFORMATION, null, values);
    }

    public boolean isTripUnique(String destinationName){
        return true;
    }

    private void CreateHealthMedication(String id, String index){

        int indexId = Integer.parseInt(index);
        ContentValues values = new ContentValues();
        values.put(Database.KEY_MEDICATION_ID,index);
        values.put(Database.KEY_MEDICATION_NAME,healthMedicationList.get(indexId).getmMedicationName());
        values.put(Database.KEY_COUNTRY_ID,id);
        values.put(Database.KEY_GENERAL_IMAGE_URI, healthMedicationList.get(indexId).getmGeneralImage());
        values.put(Database.KEY_MEDICATION_BRAND_NAME, healthMedicationList.get(indexId).getmBrandNames());
        values.put(Database.KEY_MEDICATION_DESCRIPTION, healthMedicationList.get(indexId).getmDescription());
        values.put(Database.KEY_MEDICATION_SIDE_EFFECTS, healthMedicationList.get(indexId).getmSideEffects());
        values.put(Database.KEY_MEDICATION_STORAGE, healthMedicationList.get(indexId).getmStorage());
        values.put(Database.KEY_MEDICATION_NOTES, healthMedicationList.get(indexId).getmNotes());

        mDatabase.getDb().insert(Database.TABLE_HEALTH_MEDICATION, null, values);

    }

    private void CreateHealthCondition(String id, String index){

        int indexId = Integer.parseInt(index);
        ContentValues values = new ContentValues();
        values.put(Database.KEY_CONDITION_ID,index);
        values.put(Database.KEY_CONDITION_NAME,healthConditionList.get(indexId).name);
        values.put(Database.KEY_COUNTRY_ID,id);
        values.put(Database.KEY_GENERAL_IMAGE_URI, healthConditionList.get(indexId).images.version3.sourceUrl.replace(" ", "%20"));
        values.put(Database.KEY_CONDITION_DESCRIPTION,healthConditionList.get(indexId).content.description);
        values.put(Database.KEY_CONDITION_SYMPTOMS,healthConditionList.get(indexId).content.symptoms);
        values.put(Database.KEY_CONDITION_PREVENTION,healthConditionList.get(indexId).content.prevention);

        mDatabase.getDb().insert(Database.TABLE_HEALTH_CONDITION, null, values);

    }

    private void CreateTrip(int position, String generalImageUri){
        Destination destination = mDestinationList.get(position);
//        Trip trip = new Trip(destination.getCountry());


        ContentValues values = new ContentValues();
        values.put(Database.KEY_DESTINATION_COUNTRY, destination.getCountry());
        values.put(Database.KEY_COUNTRY_ID, destination.getId());
        values.put(Database.KEY_GENERAL_IMAGE_URI, generalImageUri);


        mDatabase.getDb().insert(Database.TABLE_TRIPS, null, values);
        mDatabase.getDb().close();

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
