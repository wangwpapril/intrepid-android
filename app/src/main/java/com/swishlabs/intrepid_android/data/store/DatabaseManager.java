package com.swishlabs.intrepid_android.data.store;


import android.content.Context;
import android.database.Cursor;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.data.api.model.HealthCondition;
import com.swishlabs.intrepid_android.data.api.model.Trip;
import com.swishlabs.intrepid_android.util.StringUtil;

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
//        String countQuery = "SELECT  * FROM " + Database.TABLE_HEALTH_CONDITION;
        String countQuery = "SELECT * FROM " + Database.TABLE_HEALTH_CONDITION
                + " WHERE " + Database.KEY_COUNTRY_ID  +" = " + id;

        Cursor cursor = database.getDb().rawQuery(countQuery,null);
        return cursor.getCount();
    }

    public static ArrayList<HealthCondition> getHealthConArray(Database database, String id){

        ArrayList<HealthCondition> conList = new ArrayList<>();

        String countQuery = "SELECT * FROM " + Database.TABLE_HEALTH_CONDITION
                + " WHERE " + Database.KEY_COUNTRY_ID  +" = " + id;

        Cursor cursor = database.getDb().rawQuery(countQuery,null);
        if(cursor.moveToFirst()) {
//            while (cursor.isAfterLast() == false) {
//                HealthCondition hcDis = new HealthCondition( Integer.valueOf(cursor.getString(0)),
//                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
//                        cursor.getString(5));
//                conList.add(hcDis);
//                cursor.moveToNext();
//
//            }

        }


        return conList;

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

    public static ArrayList<Trip> getTripArray(Database database){
        ArrayList<Trip> tripList= new ArrayList<>();
        Cursor  cursor = database.getDb().rawQuery("select * from " + Database.TABLE_TRIPS, null);

        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {
//                (int id, String destinationName, String destinationId, String generalImage)
                String string0 = cursor.getString(0);
//                String string1 = cursor.getString(1);
//                String string2 = cursor.getString(2);
//                String string3 = cursor.getString(3);
                Trip trip = new Trip(Integer.valueOf(string0),cursor.getString(1), cursor.getString(3), cursor.getString(2));
                tripList.add(trip);
                cursor.moveToNext();
            }
        }
        return tripList;
    }


    public static void deleteTrip(int id, Database database){
        database.getDb().delete(Database.TABLE_TRIPS, Database.KEY_ID + "=" + id, null);
        database.getDb().close();
    }
}
