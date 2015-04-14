package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by will on 15/4/14.
 */
public class HealthConditionDis {
    public int id;
    public String mConditionName;
    public String mCountryId;
    public String mGeneralImage;
    public String mDescription;
    public String mSymptoms;
    public String mPrevention;

    public HealthConditionDis(int id, String name, String countryId, String imgUrl, String description, String symptoms, String prevention){
        this.id = id;
        this.mConditionName = name;
        this.mCountryId = countryId;
        this.mGeneralImage = imgUrl;
        this.mDescription = description;
        this.mSymptoms = symptoms;
        this.mPrevention = prevention;
    }

    public String getmConditionName(){
        return mConditionName;
    }
    public int getId(){
        return id;
    }
    public String getmGeneralImage(){
        return mGeneralImage;
    }
    public String getmCountryId() { return mCountryId; }
    public String getmDescription() { return mDescription; }
    public String getmSymptoms() { return mSymptoms; }
    public String getmPrevention() { return mPrevention; }
}
