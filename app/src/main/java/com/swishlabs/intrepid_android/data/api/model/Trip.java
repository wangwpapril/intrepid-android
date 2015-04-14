package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by ryanracioppo on 2015-03-30.
 */
public class Trip {
    public int id;
    public String mDestinationName;
    public String mCountryId;
    public String mGeneralImage;
    public Trip(int id, String destinationName, String destinationId, String generalImage){
       this.mDestinationName = destinationName;
       this.mCountryId = destinationId;
       this.mGeneralImage = generalImage;
       this.id = id;
    }
    public String getCountryId(){
        return mCountryId;
    }
    public String getDestinationName(){
        return mDestinationName;
    }
    public int getId(){
        return id;
    }
    public String getGeneralImage(){
        return mGeneralImage;
    }
}
