package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by ryanracioppo on 2015-03-30.
 */
public class Trip {
    public int id;
    public String mDestinationName;

    public Trip(int id, String destinationName){
       this.mDestinationName = destinationName;
    }

    public String getDestinationName(){
        return mDestinationName;
    }
}
