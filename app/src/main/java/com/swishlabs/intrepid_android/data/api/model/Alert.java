package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by wwang on 15-04-23.
 */
public class Alert {

    public String mCountryCode;
    public String mCategory;
    public String mDescription;
    public String mStartDate;
    public String mEndDate;

    public Alert(String countrycode, String category, String description, String startDate, String endDate){
        this.mCountryCode = countrycode;
        this.mCategory = category;
        this.mDescription = description;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
    }

    public String getCountryCode() { return mCountryCode; }

    public String getCategory() { return mCategory; }

    public String getDescription() { return mDescription; }

    public String getStartDate() { return mStartDate; }

    public String getEndDate() { return mEndDate; }
}
