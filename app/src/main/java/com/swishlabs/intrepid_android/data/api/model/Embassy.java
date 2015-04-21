package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by ryanracioppo on 2015-04-15.
 */
public class Embassy {
    public String mId;
    public String mCountry;
    public String mName;

    public String getId() {
        return mId;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getName() {
        return mName;
    }

    public String getServicesOffered() {
        return mServicesOffered;
    }

    public String getFax() {
        return mFax;
    }

    public String getSource() {
        return mSource;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getHoursofOperation() {
        return mHoursofOperation;
    }

    public String getNotes() {
        return mNotes;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public String getDestinationId() {
        return mDestinationId;
    }

    public String getImage() {
        return mImage;
    }

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
    public String mImage;

    public Embassy(String id, String country, String name, String servicesOffered, String fax, String source,
        String website, String email, String address, String hoursofOperation, String notes, String telephone, String destinationId, String image) {
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
        this.mImage = image;
    }

    public Embassy(String country, String id, String image){
        this.mId = id;
        this.mCountry = country;
        this.mImage = image;
    }
    //id, country, name, servicesOffered, fax, source, website, email, address, hoursOfOperation, notes, telephone
}
