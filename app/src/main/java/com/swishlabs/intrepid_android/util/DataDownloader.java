package com.swishlabs.intrepid_android.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.swishlabs.intrepid_android.activity.DestinationsListActivity;
import com.swishlabs.intrepid_android.activity.TripFragment;
import com.swishlabs.intrepid_android.activity.TripPagesActivity;
import com.swishlabs.intrepid_android.activity.ViewDestinationActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanracioppo on 15-05-27.
 */



public class DataDownloader {

    private List<Destination> mDestinationList;
    private DestinationsListAdapter mDestinationsListAdapter;
    public static ClearEditText mEditTextSearch;
    private int mCallbackCount = 0;

    private List<HealthCondition> healthConditionList;
    private List<HealthMedicationDis> healthMedicationList;
    private List<Alert> mAlertList;

    public DestinationsListActivity mActivity;
    public DatabaseManager mDatabaseManager;
    public Database mDatabase;
    public Context mContext;
    public Destination mDestination;
    public String mDestinationId;
    public String mCurrencyCode;
    public TripPagesActivity mTripPagesActivity;
    public String mTripDatabaseId;
    public boolean mIsDestinationNew;

    public void initializeDownload(Context context, Destination destination, DestinationsListActivity activity, String destinationId, TripPagesActivity tripPagesActivity){
        mContext =context;
        mActivity = activity;
        mDestination = destination;

        if (destination==null) {
            mIsDestinationNew = true;
            mDestinationId = destinationId;
            mTripPagesActivity = tripPagesActivity;
        }else{
            mIsDestinationNew = false;
        }
        LoadTripFromApi(0, null);
    }

    public void loadDestination(){

    }

    public boolean mIsTripUnique;

    public void loadDatabase(){
        mDatabaseManager = new DatabaseManager(mContext);
        mDatabase = mDatabaseManager.openDatabase("Intrepid.db");
    }

    public void LoadTripFromApi(int test, final String rate){
        Logger.d("LoadTripFromApi");
        loadDatabase();
        if (mDestination!=null) {
            mDestinationId = mDestination.getId();
        }
        if (true) {
            mIsTripUnique = DatabaseManager.isTripUnique(mDatabase, mDestinationId);
            if (mDestination!=null) {
                mCurrencyCode = mDestination.getCurrencyCode();
            }

            IControllerContentCallback icc = new IControllerContentCallback() {

                public void handleSuccess(String content) {

                    JSONObject destination;
                    JSONArray healthCondition, healthMedication;
                    String countryId;
                    try {
                        destination = new JSONObject(content).getJSONObject("destination");
                        if (mIsDestinationNew){
                            mDestination = new Destination(destination);
                            mCurrencyCode = mDestination.getCurrencyCode();
                        }
                        JSONObject country = destination.getJSONObject("country");
                        if (country != null) {
                            String countryCode = country.getString("country_code");
                            saveEmbassyInformation(mDestination.getId(), countryCode);
                            fetchAlerts(countryCode);
                        }
                        countryId = destination.optString("id");
                        SharedPreferenceUtil.setString(Enums.PreferenceKeys.currentCountryId.toString(), countryId);
                        if (destination.has("health_conditions")) {
                            healthCondition = destination.getJSONArray("health_conditions");
                            int len = healthCondition.length();
                            healthConditionList = new ArrayList<HealthCondition>(len);

                            for (int i = 0; i < len; i++) {
                                healthConditionList.add(new HealthCondition(healthCondition.getJSONObject(i)));
                                CreateHealthCondition(destination.optString("id"), String.valueOf(i));

                            }

                        }

                        if (destination.has("medications")) {
                            healthMedication = destination.getJSONArray("medications");
                            int len = healthMedication.length();
                            healthMedicationList = new ArrayList<HealthMedicationDis>(len);

                            for (int i = 0; i < len; i++) {
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
                        final String general_image_url = images.getJSONObject("intro").getJSONObject("versions").getJSONObject("3x")
                                .getString("source_url");

                        saveDestinationInformation(destination, images, mCurrencyCode, rate);
                        String encodedURL = general_image_url.replace(" ", "%20");
                        CreateTrip(0, encodedURL);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                public void handleError(Exception e) {

                }
            };

            String token = null;
            token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);
            ControllerContentTask cct = new ControllerContentTask(
                    Constants.BASE_URL + "destinations/" + mDestinationId + "?token=" + token, icc,
                    Enums.ConnMethod.GET, false);
            String ss = null;
            cct.execute(ss);
        }else{
            List<Trip> tripList = DatabaseManager.getTripArray(mDatabase, SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null));
            for (int i = 0; i<tripList.size(); i++){
                if (tripList.get(i).getCountryId().equals(mDestinationId)){
                    SharedPreferenceUtil.setInt(TripPagesActivity.getInstance(), Enums.PreferenceKeys.currentPage.toString(), i+1);
                    break;
                }
            }
            SharedPreferenceUtil.setString(Enums.PreferenceKeys.currentCountryId.toString(), mDestinationId);
            if (mActivity!=null) {
                mActivity.redirectToTripOverview(mDestinationId);

            }else if (mTripPagesActivity!=null){
                mTripPagesActivity.redirectToTripOverview(mDestinationId);
            }
//            Intent intent = new Intent(mActivity, ViewDestinationActivity.class);
//            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.putExtra("destinationId", destinationId);
//            intent.putExtra("firstTimeFlag", "1");
//            mActivity.startActivity(intent);
        }
    }

    private void saveEmbassyInformation(final String destinationId, String countryCode){
        Logger.d("saveEmbassyInformation");

        IControllerContentCallback icc = new IControllerContentCallback() {

            public void handleSuccess(String content) {
                JSONObject diplomaticOffices;
                try {
                    diplomaticOffices = new JSONObject(content);
                    JSONArray diplomaticOfficesList = diplomaticOffices.getJSONArray("diplomatic_office");
                    for(int i = 0;i < diplomaticOfficesList.length();i++){
                        JSONObject diplomaticOffice = diplomaticOfficesList.getJSONObject(i);
                        String id = diplomaticOffice.getString("id");
                        String country = diplomaticOffice.getString("country");
                        String name = diplomaticOffice.getString("name");
                        JSONObject embassyContent = diplomaticOffice.getJSONObject("content");
                        JSONObject embassyImage = diplomaticOffice.getJSONObject("images").getJSONObject("embassy");
                        String formattedEmbassyImage = embassyImage.getString("source_url").replace(" ", "%20");
                        String servicesOffered = embassyContent.getString("services_offered");
                        String fax = embassyContent.getString("fax");
                        String source = embassyContent.getString("source");
                        String website = embassyContent.getString("website");
                        String email = embassyContent.getString("email");
                        String address = embassyContent.getString("address");
                        String hoursOfOperation = embassyContent.getString("hours_of_operation");
                        String notes = embassyContent.getString("notes");
                        String telephone = embassyContent.getString("telephone");

                        ContentValues values = new ContentValues();
                        values.put(Database.KEY_EMBASSY_ID, id);
                        values.put(Database.KEY_EMBASSY_COUNTRY, country);
                        values.put(Database.KEY_EMBASSY_NAME, name);
                        values.put(Database.KEY_EMBASSY_SERVICES_OFFERED, servicesOffered);
                        values.put(Database.KEY_EMBASSY_FAX, fax);
                        values.put(Database.KEY_EMBASSY_SOURCE, source);
                        values.put(Database.KEY_EMBASSY_WEBSITE, website);
                        values.put(Database.KEY_EMBASSY_EMAIL, email);
                        values.put(Database.KEY_EMBASSY_ADDRESS, address);
                        values.put(Database.KEY_EMBASSY_HOURS_OF_OPERATION, hoursOfOperation);
                        values.put(Database.KEY_EMBASSY_NOTES, notes);
                        values.put(Database.KEY_EMBASSY_TELEPHONE, telephone);
                        values.put(Database.KEY_EMBASSY_DESTINATION_ID, destinationId);
                        values.put(Database.KEY_EMBASSY_IMAGE, formattedEmbassyImage);
                        if (mIsTripUnique) {
                            mDatabase.getDb().insert(Database.TABLE_EMBASSY, null, values);
                        }else{
                            mDatabase.getDb().update(Database.TABLE_EMBASSY, values, Database.KEY_ID+" ="+mTripDatabaseId, null);
                        }
                        if (mCallbackCount == 1) {
//                            mDatabase.getDb().close();
                        }else{
                            mCallbackCount = mCallbackCount + 1;
                        }

                    }


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

        String ss = null;
        cct.execute(ss);
    }

    private void saveDestinationInformation(JSONObject destination, JSONObject images, String currencyCode, String rate) throws JSONException {
        Logger.d("saveDestinationInfo");

        JSONObject content = destination.getJSONObject("content");
        String destinationId = destination.getString("id");
        String countryName = destination.getJSONObject("country").getString("name");
        String countryCode = destination.getJSONObject("country").getString("country_code");
        String currCode = destination.getJSONObject("country").getString("currency_code");
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
        String intro_image_url = images.getJSONObject("intro").getJSONObject("versions").getJSONObject("3x").getString("source_url").replace(" ", "%20");
        String security_image_url = images.getJSONObject("security").getJSONObject("versions").getJSONObject("3x").getString("source_url").replace(" ", "%20");
        String overview_image_url = images.getJSONObject("overview").getJSONObject("versions").getJSONObject("3x").getString("source_url").replace(" ", "%20");
        String culture_image_url = images.getJSONObject("culture").getJSONObject("versions").getJSONObject("3x").getString("source_url").replace(" ", "%20");
        String currency_image_url = null;
        if(images.has("currency")) {
            currency_image_url = images.getJSONObject("currency").getString("source_url").replace(" ", "%20");
        }

        String health_care_quality = content.getString("health_care_quality");
        String vaccines_pre_trip_medical = content.getString("vaccinations_and_pre_trip_medical");
        String health_conditions = content.getString("health_conditions");
        String emergency_numbers = content.getString("emergency_numbers");
        String medical_image_url = images.getJSONObject("medical").getJSONObject("versions").getJSONObject("2x")
                .getString("source_url").replace(" ", "%20");

        String transportation = content.getString("transportation");
        String holidays = content.getString("holidays");


        ContentValues values = new ContentValues();
        values.put(Database.KEY_DESTINATION_ID, destinationId);
        values.put(Database.KEY_COUNTRY_NAME, countryName);
        values.put(Database.KEY_COUNTRY_CODE, countryCode);
        values.put(Database.KEY_COMMUNICATIONS, communicationsInfrastructure);
        values.put(Database.KEY_OTHER_CONCERNS, otherConcerns);
        values.put(Database.KEY_DEVELOPMENT, development);
        values.put(Database.KEY_LOCATION, location);
        values.put(Database.KEY_CULTURAL_NORMS, cultural_norms);
        values.put(Database.KEY_SOURCES, sources);
        values.put(Database.KEY_CURRENCY, currency);
        values.put(Database.KEY_CURRENCY_CODE, currCode);
        values.put(Database.KEY_CURRENCY_RATE, rate);
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
        values.put(Database.KEY_EMERGENCY_NUMBER, emergency_numbers);
        values.put(Database.KEY_HEALTH_CARE, health_care_quality);
        values.put(Database.KEY_VACCINATION, vaccines_pre_trip_medical);
        values.put(Database.KEY_HEALTH_CONDITION, health_conditions);
        values.put(Database.KEY_IMAGE_MEDICAL, medical_image_url);
        values.put(Database.KEY_TRANSPORTATION, transportation);
        values.put(Database.KEY_HOLIDAYS, holidays);
        if (mIsTripUnique) {
            mDatabase.getDb().insert(Database.TABLE_DESTINATION_INFORMATION, null, values);
        }else{
            mDatabase.getDb().update(Database.TABLE_DESTINATION_INFORMATION, values, Database.KEY_ID+" ="+mTripDatabaseId, null);
        }

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

        if (mIsTripUnique) {
            mDatabase.getDb().insert(Database.TABLE_HEALTH_MEDICATION, null, values);
        }else{
            mDatabase.getDb().update(Database.TABLE_HEALTH_MEDICATION, values, Database.KEY_ID+" ="+mTripDatabaseId, null);
        }


    }

    private void CreateHealthCondition(String id, String index){

        int indexId = Integer.parseInt(index);
        ContentValues values = new ContentValues();
        values.put(Database.KEY_CONDITION_ID,index);
        values.put(Database.KEY_CONDITION_NAME, healthConditionList.get(indexId).name);
        values.put(Database.KEY_COUNTRY_ID,id);
        values.put(Database.KEY_GENERAL_IMAGE_URI, healthConditionList.get(indexId).images.version3.sourceUrl.replace(" ", "%20"));
        values.put(Database.KEY_CONDITION_DESCRIPTION,healthConditionList.get(indexId).content.description);
        values.put(Database.KEY_CONDITION_SYMPTOMS,healthConditionList.get(indexId).content.symptoms);
        values.put(Database.KEY_CONDITION_PREVENTION,healthConditionList.get(indexId).content.prevention);

        if (mIsTripUnique) {
            mDatabase.getDb().insert(Database.TABLE_HEALTH_CONDITION, null, values);
        }else{
            mDatabase.getDb().update(Database.TABLE_HEALTH_CONDITION, values, Database.KEY_ID+" ="+mTripDatabaseId, null);
        }

    }

    private void CreateTrip(int position, String generalImageUri){
        Logger.d("CreateTrip");

        Destination destination = mDestination;
//        Trip trip = new Trip(destination.getCountry());


        ContentValues values = new ContentValues();
        values.put(Database.KEY_DESTINATION_COUNTRY, destination.getCountry());
        values.put(Database.KEY_COUNTRY_ID, destination.getId());
        values.put(Database.KEY_GENERAL_IMAGE_URI, generalImageUri);
        values.put(Database.KEY_TRIP_USER_ID, SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null));
        if (mActivity!=null) {
            Common.sendDirectTracking(mActivity, "New Trip", "Add Trip", destination.getCountry(), -1);
        }

        if (mIsTripUnique){
            mDatabase.getDb().insert(Database.TABLE_TRIPS, null, values);
        }
        if (mCallbackCount == 1) {
            mDatabase.getDb().close();
        }else{
            mCallbackCount = mCallbackCount + 1;
        }
        SharedPreferenceUtil.setString(Enums.PreferenceKeys.currentCountryId.toString(), destination.getId());
        List<Trip> tripList = DatabaseManager.getTripArray(mDatabase, SharedPreferenceUtil.getString(Enums.PreferenceKeys.userId.toString(), null));
        for (int i = 0; i<tripList.size(); i++){
            if (tripList.get(i).getCountryId().equals(mDestinationId)){
                Context context;
                if (mActivity!=null) {
                   context = mActivity;
                }else if (mTripPagesActivity!=null){
                    context = mTripPagesActivity;
                }else{
                    context = TripPagesActivity.getInstance();
                }
                SharedPreferenceUtil.setInt(context, Enums.PreferenceKeys.currentPage.toString(), i + 1);
                break;
            }
        }
        if (mActivity!=null) {
            mActivity.redirectToTripOverview(mDestinationId);
        }else{
            mTripPagesActivity.redirectToTripOverview(mDestinationId);
        }
//        Intent intent = new Intent(DestinationsListActivity.this, ViewDestinationActivity.class);
//        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("destinationId", destination.getId());
//        intent.putExtra("firstTimeFlag","1");
//        startActivity(intent);

    }

    private void fetchAlerts(final String countrycode) {
        Logger.d("fetchAlerts");

        IControllerContentCallback icc = new IControllerContentCallback() {

            public void handleSuccess(String content) {
                JSONArray alertArray;
                try {
                    alertArray = new JSONObject(content).getJSONArray("content");
                    int len = alertArray.length();
                    mAlertList = new ArrayList<Alert>(len);
                    SimpleDateFormat to = new SimpleDateFormat("MMM d, yyyy");
                    SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy");

                    for(int i = 0; i < len; i++){
                        JSONObject alertObj = alertArray.getJSONObject(i);
                        String cate = alertObj.optString("category");
                        String desc = alertObj.optString("description");
                        String start = alertObj.optString("start");
                        String end = alertObj.optString("end");
                        try {
                            start = to.format(from.parse(start));
                            end = to.format(from.parse(end));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Alert mAlert = new Alert(countrycode,cate, desc, start, end);
                        mAlertList.add(mAlert);

                        saveAlert(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void handleError(Exception e){

            }
        };

        String token = SharedPreferenceUtil.getString(Enums.PreferenceKeys.token.toString(), null);

        ControllerContentTask cct = new ControllerContentTask(
                Constants.BASE_URL+"alerts/"+countrycode+"?token="+token, icc,
                Enums.ConnMethod.GET,false);

        String ss = null;
        cct.execute(ss);


    }

    private void saveAlert(int index){
        ContentValues values = new ContentValues();
        values.put(Database.KEY_COUNTRY_CODE, mAlertList.get(index).getCountryCode());
        values.put(Database.KEY_ALERT_CATEGORY, mAlertList.get(index).getCategory());
        values.put(Database.KEY_ALERT_DESCRIPTION, mAlertList.get(index).getDescription());
        values.put(Database.KEY_ALERT_STARTDATE, mAlertList.get(index).getStartDate());
        values.put(Database.KEY_ALERT_ENDDATE, mAlertList.get(index).getEndDate());
        if (mIsTripUnique) {
            mDatabase.getDb().insert(Database.TABLE_ALERT, null, values);
        }else{
            mDatabase.getDb().update(Database.TABLE_ALERT, values, Database.KEY_ID+" ="+mTripDatabaseId, null);
        }

    }



}
