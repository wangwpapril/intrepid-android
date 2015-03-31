package com.swishlabs.intrepid_android.data.store;


import android.content.Context;
import android.database.Cursor;

import com.swishlabs.intrepid_android.MyApplication;

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
}
