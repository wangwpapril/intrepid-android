package com.swishlabs.intrepid_android.data.api.model;

/**
 * Created by will on 15/4/14.
 */
public class HealthMedicationDis {
    public int id;
    public String mMedicationName;
    public String mCountryId;
    public String mGeneralImage;
    public String mBrandNames;
    public String mDescription;
    public String mSideEffects;
    public String mStorage;
    public String mNotes;

    public HealthMedicationDis(int id, String Mname, String countryId, String imgUrl,
                               String Bname,String description, String effect, String storage,
                               String notes){
        this.id = id;
        this.mMedicationName = Mname;
        this.mCountryId = countryId;
        this.mGeneralImage = imgUrl;
        this.mBrandNames = Bname;
        this.mDescription = description;
        this.mSideEffects = effect;
        this.mStorage = storage;
        this.mNotes = notes;
    }

    public HealthMedicationDis(){

    }

    public String getmMedicationName(){
        return mMedicationName;
    }
    public int getId(){
        return id;
    }
    public String getmGeneralImage(){
        return mGeneralImage;
    }
    public String getmCountryId() { return mCountryId; }
    public String getmDescription() { return mDescription; }
    public String getmBrandNames() { return mBrandNames; }
    public String getmSideEffects() { return mSideEffects; }
    public String getmStorage() { return mStorage; }
    public String getmNotes() { return mNotes; }
}
