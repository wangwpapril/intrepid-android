package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by ryanracioppo on 2015-04-14.
 */
public class DestinationInformation {
    public String mDestinationId;
    public String mCommunicationsInfrastructure;
    public String mOtherConcerns;
    public String mDevelopment;
    public String mLocation;
    public String mCulturalNorms;
    public String mSources;
    public String mCurrency;
    public String mReligion;
    public String mTimeZone;
    public String mSafety;
    public String mTypeOfGovernment;
    public String mVisaMapAttributionUri;
    public String mElectricity;
    public String mEthnicMakeup;
    public String mLanguageInfo;
    public String mVisaRequirements;
    public String mClimate;
    public String mImage1;
    public String mImage2;
    public String mImage3;

    public DestinationInformation(String destinationId, String communicationsInfrastructure, String otherConcerns, String development, String location, String culturalNorms, String sources,
        String currency, String religion, String timeZone, String safety, String typeOfGovernment, String visaMapAttributionUri, String electricity,
        String ethnicMakeup, String languageInfo, String visaRequirements, String climate, String image1, String image2, String image3){
        this.mDestinationId = destinationId;
        this.mCommunicationsInfrastructure = communicationsInfrastructure;
        this.mOtherConcerns = otherConcerns;
        this.mDevelopment = development;
        this.mLocation = location;
        this.mCulturalNorms = culturalNorms;
        this.mSources = sources;
        this.mCurrency = currency;
        this.mReligion = religion;
        this.mTimeZone = timeZone;
        this.mSafety = safety;
        this.mTypeOfGovernment = typeOfGovernment;
        this.mVisaMapAttributionUri = visaMapAttributionUri;
        this.mElectricity = electricity;
        this.mEthnicMakeup = ethnicMakeup;
        this.mLanguageInfo = languageInfo;
        this.mVisaRequirements = visaRequirements;
        this.mClimate = climate;
        this.mImage1 = image1;
        this.mImage2 = image2;
        this.mImage3 = image3;
    }

    public String getCommunicationsInfrastructure() {
        return mCommunicationsInfrastructure;
    }

    public String getOtherConcerns() {
        return mOtherConcerns;
    }

    public String getDevelopment() {
        return mDevelopment;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getCulturalNorms() {
        return mCulturalNorms;
    }

    public String getSources() {
        return mSources;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getReligion() {
        return mReligion;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public String getSafety() {
        return mSafety;
    }

    public String getTypeOfGovernment() {
        return mTypeOfGovernment;
    }

    public String getVisaMapAttributionUri() {
        return mVisaMapAttributionUri;
    }

    public String getElectricity() {
        return mElectricity;
    }

    public String getEthnicMakeup() {
        return mEthnicMakeup;
    }

    public String getLanguageInfo() {
        return mLanguageInfo;
    }

    public String getVisaRequirements() {
        return mVisaRequirements;
    }

    public String getClimate() {
        return mClimate;
    }



}

