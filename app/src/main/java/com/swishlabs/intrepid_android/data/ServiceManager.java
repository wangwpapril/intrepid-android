package com.swishlabs.intrepid_android.data;

import android.content.Context;
import com.swishlabs.intrepid_android.data.store.DatabaseManager;

public class ServiceManager {
    static Context context;
    static DatabaseManager databaseManager;
    public static void init(Context ctx) {
        context = ctx;
        databaseManager = new DatabaseManager(ctx);
    }
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
