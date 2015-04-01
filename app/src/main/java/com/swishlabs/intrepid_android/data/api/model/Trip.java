package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by ryanracioppo on 2015-03-30.
 */
public class Trip {
    public int id;
    public String mDestinationName;
    public String mCountryId;

    public Trip(int id, String destinationName, String destinationId){
       this.mDestinationName = destinationName;
       this.mCountryId = destinationId;
    }

    public String getDestinationName(){
        return mDestinationName;
    }

    public int getId(){
        return id;
    }
}
