package com.intrepid.travel.store;


import java.util.HashMap;
import java.util.Map;

import com.intrepid.travel.MyApplication;

import android.content.Context;


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
}
