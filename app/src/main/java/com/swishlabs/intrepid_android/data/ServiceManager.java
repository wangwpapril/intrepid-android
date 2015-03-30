package com.intrepid.travel;

import com.intrepid.travel.store.DatabaseManager;

import android.content.Context;



public class ServiceManager {
    static Context context;

//    static NetworkService networkService;
    static DatabaseManager databaseManager;
//    static DataService dataService;

    public static void init(Context ctx) {
        context = ctx;
//        dataService = new DataService();
//        networkService = new NetworkService();
        databaseManager = new DatabaseManager(ctx);
    }

/*    public static NetworkService getNetworkService() {
        return networkService;
    }
*/    
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
/*    public static DataService getDataService() {
        return dataService;
    }
 */   
}
