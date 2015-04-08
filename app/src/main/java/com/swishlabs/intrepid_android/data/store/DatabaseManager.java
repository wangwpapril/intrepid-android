package com.swishlabs.intrepid_android.data.store;


import android.content.Context;
import android.database.Cursor;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.data.api.model.Trip;

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

    public static int getTripCount(Database database) {
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
        return trip;
    }
}
