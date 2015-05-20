package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by ryanracioppo on 2015-04-14.
 */
public class DestinationInformation {
    public String mDestinationId;
    public String mCountryName;
    public String mCountryCode;
    public String mCommunicationsInfrastructure;
    public String mOtherConcerns;
    public String mDevelopment;
    public String mLocation;
    public String mCulturalNorms;
    public String mSources;
    public String mCurrency;
    public String mCurrencyCode;
    public String mCurrencyRate;
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



    public String mTransportation;
    public String mHolidays;

    public String mEmergencyNumber;
    public String mHealthCare;
    public String mVacciMedical;
    public String mHealthCondition;
    public String mImageMedical;



    public String mImageSecurity;
    public String mImageOverview;
    public String mImageCulture;
    public String mImageIntro;
    public String mImageCurrency;

    public DestinationInformation(String destinationId, String name, String countryCode, String communicationsInfrastructure, String otherConcerns, String development, String location, String culturalNorms, String sources,
        String currency, String currencyCode, String currencyRate, String religion, String timeZone, String safety, String typeOfGovernment, String visaMapAttributionUri, String electricity,
        String ethnicMakeup, String languageInfo, String visaRequirements, String climate, String image1, String image2, String image3, String image4, String image5, String emergency, String healthcare,
        String vaccination, String healthcondition, String imageMedical, String transportation, String holidays){
        this.mDestinationId = destinationId;
        this.mCountryName = name;
        this.mCountryCode = countryCode;
        this.mCommunicationsInfrastructure = communicationsInfrastructure;
        this.mOtherConcerns = otherConcerns;
        this.mDevelopment = development;
        this.mLocation = location;
        this.mCulturalNorms = culturalNorms;
        this.mSources = sources;
        this.mCurrency = currency;
        this.mCurrencyCode = currencyCode;
        this.mCurrencyRate = currencyRate;
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
        this.mImageSecurity = image1;
        this.mImageOverview = image2;
        this.mImageCulture = image3;
        this.mImageIntro = image4;
        this.mImageCurrency = image5;
        this.mEmergencyNumber = emergency;
        this.mHealthCare = healthcare;
        this.mVacciMedical = vaccination;
        this.mHealthCondition = healthcondition;
        this.mImageMedical = imageMedical;
        this.mTransportation = transportation;
        this.mHolidays = holidays;
    }

    public String getCountryName() { return mCountryName; }

    public String getCountryCode() { return mCountryCode; }

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

    public String getCurrencyCode() { return mCurrencyCode; }

    public String getCurrencyRate() { return mCurrencyRate; }

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

    public String getImageSecurity() {
        return mImageSecurity;
    }

    public String getImageOverview() {
        return mImageOverview;
    }

    public String getImageCulture() {
        return mImageCulture;
    }

    public String getImageIntro() {
        return mImageIntro;
    }

    public String getImageCurrency() {
        return mImageCurrency;
    }

    public String getEmergencyNumber() { return mEmergencyNumber; }

    public String getHealthCare() { return mHealthCare; }

    public String getVacciMedical() { return mVacciMedical; }

    public String getHealthCondition() { return mHealthCondition; }

    public String getImageMedical() { return mImageMedical; }

    public String getTransportation() {
        return mTransportation;
    }

    public String getmHolidays() {
        return mHolidays;
    }



}

