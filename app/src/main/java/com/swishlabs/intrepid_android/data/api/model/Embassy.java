package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by ryanracioppo on 2015-04-15.
 */
public class Embassy {
    public String mId;
    public String mCountry;
    public String mName;
    public String mServicesOffered;
    public String mFax;
    public String mSource;
    public String mWebsite;
    public String mEmail;
    public String mAddress;
    public String mHoursofOperation;
    public String mNotes;
    public String mTelephone;
    public String mDestinationId;

    public Embassy(String id, String country, String name, String servicesOffered, String fax, String source,
        String website, String email, String address, String hoursofOperation, String notes, String telephone, String destinationId) {
        this.mId = id;
        this.mCountry = country;
        this.mName = name;
        this.mServicesOffered = servicesOffered;
        this.mFax = fax;
        this.mSource = source;
        this.mWebsite = website;
        this.mEmail = email;
        this.mAddress = address;
        this.mHoursofOperation = hoursofOperation;
        this.mNotes = notes;
        this.mTelephone = telephone;
        this.mDestinationId = destinationId;
    }
    //id, country, name, servicesOffered, fax, source, website, email, address, hoursOfOperation, notes, telephone
}
