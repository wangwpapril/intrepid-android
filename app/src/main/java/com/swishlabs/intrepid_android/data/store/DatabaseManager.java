package com.swishlabs.intrepid_android.data.store;


import android.content.Context;
import android.database.Cursor;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.data.api.model.Alert;
import com.swishlabs.intrepid_android.data.api.model.Currency;
import com.swishlabs.intrepid_android.data.api.model.DestinationInformation;
import com.swishlabs.intrepid_android.data.api.model.Embassy;
import com.swishlabs.intrepid_android.data.api.model.HealthConditionDis;
import com.swishlabs.intrepid_android.data.api.model.HealthMedicationDis;
import com.swishlabs.intrepid_android.data.api.model.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private Map<String, Database> dbCache;
    private Context context;
    public DatabaseManager() {
        this(MyApplication.getInstance());
    }
    public DatabaseManager(Context context) {
        this.context = context;
        dbCache = new HashMap<String, Database>();
    }
    public Database openDatabase(String name) {
        if (dbCache.containsKey(name)) {
            return dbCache.get(name);
        } else {
            Database db = new Database(name);
            dbCache.put(name, db);
            return db;
        }
    }

    public static int getHealthConCount(Database database, String id){
        String countQuery = "SELECT * FROM " + Database.TABLE_HEALTH_CONDITION
                + " WHERE " + Database.KEY_COUNTRY_ID  +" = " + id;

        Cursor cursor = database.getDb().rawQuery(countQuery,null);
        return cursor.getCount();
    }

    public static ArrayList<HealthConditionDis> getHealthConArray(Database database, String id){

        ArrayList<HealthConditionDis> conList = new ArrayList<>();

        String countQuery = "SELECT * FROM " + Database.TABLE_HEALTH_CONDITION
                + " WHERE " + Database.KEY_COUNTRY_ID  +" = " + id;

        Cursor cursor = database.getDb().rawQuery(countQuery,null);
        if(cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                HealthConditionDis hcDis = new HealthConditionDis( Integer.valueOf(cursor.getString(1)),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7));
                conList.add(hcDis);
                cursor.moveToNext();

            }

        }

        return conList;

    }

    public static ArrayList<HealthMedicationDis> getHealthMedArray(Database database, String id){

        ArrayList<HealthMedicationDis> medList = new ArrayList<>();

        String countQuery = "SELECT * FROM " + Database.TABLE_HEALTH_MEDICATION
                + " WHERE " + Database.KEY_COUNTRY_ID  +" = " + id;

        Cursor cursor = database.getDb().rawQuery(countQuery,null);
        if(cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                HealthMedicationDis hmDis = new HealthMedicationDis( Integer.valueOf(cursor.getString(1)),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                medList.add(hmDis);
                cursor.moveToNext();

            }

        }

        return medList;

    }

//    public static Currency getCurrency(Database database, String code) {
//        String countQuery = "SELECT * FROM " + Database.TABLE_CURRENCY
//                + " WHERE " + Database.KEY_CURRENCY_CODE  +" = " + code;
//
//        Cursor cursor = database.getDb().rawQuery(countQuery,null);
//        if(cursor != null)
//            cursor.moveToFirst();
//        Currency mCurrency = new Currency( cursor.getString(1), cursor.getString(2));
//        return mCurrency;
//
//    }

    public static Currency getCurrency(String code, Database database) {
        Cursor cursor = database.getDb().query(Database.TABLE_CURRENCY, new String[]{Database.KEY_ID,
                        Database.KEY_CURRENCY_CODE, Database.KEY_GENERAL_IMAGE_URI}, Database.KEY_CURRENCY_CODE + "=?",
                new String[]{code}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Currency currency = new Currency(cursor.getString(1),cursor.getString(2));
         return currency;
    }

    public static int getTripCount(Database database, String id) {
        String countQuery = "SELECT  * FROM " + Database.TABLE_TRIPS;
        Cursor cursor = database.getDb().rawQuery(countQuery, null);
        return cursor.getCount();
    }
    public static Trip getTrip(int id, Database database) {
        Cursor cursor = database.getDb().query(Database.TABLE_TRIPS, new String[]{Database.KEY_ID,
                        Database.KEY_DESTINATION_COUNTRY, Database.KEY_COUNTRY_ID, Database.KEY_GENERAL_IMAGE_URI}, Database.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Trip trip = new Trip(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        database.getDb().close();
        return trip;
    }

    public static ArrayList<Trip> getTripArray(Database database, String user_Id){
        ArrayList<Trip> tripList= new ArrayList<>();
        Cursor  cursor = database.getDb().rawQuery("select * from " + Database.TABLE_TRIPS + " WHERE " + Database.KEY_TRIP_USER_ID + " = "+user_Id, null);

        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {
//                (int id, String destinationName, String destinationId, String generalImage)
                String string0 = cursor.getString(0);
                Trip trip = new Trip(Integer.valueOf(string0),cursor.getString(1), cursor.getString(4), cursor.getString(2));
                tripList.add(trip);
                cursor.moveToNext();
            }
        }
        return tripList;
    }

    public static ArrayList<Embassy> getEmbassyListArray(Database database, String countryId){
        ArrayList<Embassy> embassyList= new ArrayList<>();
        Cursor cursor = database.getDb().rawQuery("SELECT * FROM "+Database.TABLE_EMBASSY+" WHERE "+Database.KEY_EMBASSY_DESTINATION_ID+"=?" ,
                new String [] {countryId});
//        Cursor  cursor = database.getDb().rawQuery("SELECT * FROM " + Database.TABLE_TRIPS + " WHERE "+Database.KEY_EMBASSY_DESTINATION_ID+" = "+'"'+countryId+'"', null);

        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {

                Embassy embassy = new Embassy(cursor.getString(3), cursor.getString(0), cursor.getString(14));

                embassyList.add(embassy);
                cursor.moveToNext();
            }
        }
        return embassyList;
    }


    public static Embassy getEmbassy(String id, Database database) {
        Cursor cursor = database.getDb().query(Database.TABLE_EMBASSY, new String[]{Database.KEY_ID,
                        Database.KEY_EMBASSY_COUNTRY, Database.KEY_EMBASSY_NAME, Database.KEY_EMBASSY_SERVICES_OFFERED, Database.KEY_EMBASSY_FAX,
                        Database.KEY_EMBASSY_SOURCE, Database.KEY_EMBASSY_WEBSITE, Database.KEY_EMBASSY_EMAIL, Database.KEY_EMBASSY_ADDRESS,
                        Database.KEY_EMBASSY_HOURS_OF_OPERATION, Database.KEY_EMBASSY_NOTES, Database.KEY_EMBASSY_TELEPHONE,
                        Database.KEY_EMBASSY_DESTINATION_ID, Database.KEY_EMBASSY_IMAGE}, Database.KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Embassy embassy = new Embassy(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13));
        return embassy;
    }

    public static boolean isTripUnique(Database database, String destinationId){
        Cursor cursor = database.getDb().query(Database.TABLE_TRIPS, new String[]{Database.KEY_ID,
                        Database.KEY_DESTINATION_COUNTRY, Database.KEY_COUNTRY_ID, Database.KEY_GENERAL_IMAGE_URI}, Database.KEY_COUNTRY_ID + "=?",
                new String[]{destinationId}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return true;
        }else{
            return false;
        }
    }

    public static DestinationInformation getDestinationInformation(Database database, String destinationId){
        Cursor cursor = database.getDb().query(Database.TABLE_DESTINATION_INFORMATION, new String[]{Database.KEY_DESTINATION_ID,
                        Database.KEY_COUNTRY_NAME, Database.KEY_COUNTRY_CODE,Database.KEY_COMMUNICATIONS, Database.KEY_OTHER_CONCERNS, Database.KEY_DEVELOPMENT, Database.KEY_LOCATION,
                        Database.KEY_CULTURAL_NORMS, Database.KEY_SOURCES, Database.KEY_CURRENCY, Database.KEY_CURRENCY_CODE, Database.KEY_CURRENCY_RATE,
                        Database.KEY_RELIGION, Database.KEY_TIMEZONE, Database.KEY_SAFETY, Database.KEY_GOVERNMENT, Database.KEY_VISAMAP,
                        Database.KEY_ELECTRICITY, Database.KEY_ETHNIC_MAKEUP, Database.KEY_LANGUAGE_INFORMATION, Database.KEY_VISA_REQUIREMENT,
                        Database.KEY_CLIMATE_INFO, Database.KEY_IMAGE_SECURITY, Database.KEY_IMAGE_OVERVIEW, Database.KEY_IMAGE_CULTURE,
                        Database.KEY_IMAGE_INTRO, Database.KEY_IMAGE_CURRENCY}, Database.KEY_DESTINATION_ID + "=?",
                new String[]{String.valueOf(destinationId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        DestinationInformation destinationInformation = new DestinationInformation(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18),
                cursor.getString(19), cursor.getString(20), cursor.getString(21), cursor.getString(22), cursor.getString(23), cursor.getString(24),
                cursor.getString(25), cursor.getString(26));

        return destinationInformation;
    }

    public static ArrayList<Alert> getAlertList(Database database, String countryCode){
        ArrayList<Alert> alertList = new ArrayList<>();
        Cursor cursor = database.getDb().query(Database.TABLE_ALERT, new String[]{Database.KEY_COUNTRY_CODE,
        Database.KEY_ALERT_CATEGORY, Database.KEY_ALERT_DESCRIPTION, Database.KEY_ALERT_STARTDATE, Database.KEY_ALERT_ENDDATE},
                Database.KEY_COUNTRY_CODE + "=?", new String[]{countryCode}, null,null,null,null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {

                Alert alert = new Alert(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                alertList.add(alert);
                cursor.moveToNext();
            }
        }
        return alertList;

    }


    public static void deleteTrip(int id, Database database){
        database.getDb().delete(Database.TABLE_TRIPS, Database.KEY_ID + "=" + id, null);
        database.getDb().close();
    }

    public static void deleteCurrency(Database database){
        database.getDb().delete(Database.TABLE_CURRENCY, null, null);
    }

    public static void deleteAlert(String countrycode, Database database){
        database.getDb().delete(Database.TABLE_ALERT, Database.KEY_COUNTRY_CODE + "=" + countrycode, null);
    }


}
