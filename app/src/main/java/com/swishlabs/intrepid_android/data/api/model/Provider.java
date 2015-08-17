package com.swishlabs.intrepid_android.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by wwang on 15-08-04.
 */
public class Provider implements Serializable, Parcelable {

    String id;
    String name;
    String type;
    String content;
    String longitude;
    String latitude;
    String postal;
    String address;
    String contact;
    String staffName;


    public Provider() {

    }

    public String getId () { return id; }

    public String getName () { return name; }

    public String getContent () { return content; }

    public String getLongitude() { return longitude; }

    public String getLatitude() { return latitude; }

    public String getType() { return type; }

    public String getPostal() { return postal; }

    public String getAddress() { return address; }

    public String getContact() { return contact; }

    public String getStaffName() { return staffName; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) { this.content = content; }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
