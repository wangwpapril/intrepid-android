package com.swishlabs.intrepid_android.data.api.model;

import java.io.Serializable;

/**
 * Created by wwang on 15-08-04.
 */
public class Physician implements Serializable {

    String id;
    String name;
    private String gender;
    String content;
    String longitude;
    String latitude;
    String postal;
    String address;
    String contact;


    public Physician() {

    }

    public String getId () { return id; }

    public String getName () { return name; }

    public String getContent () { return content; }

    public String getLongitude() { return longitude; }

    public String getLatitude() { return latitude; }

    public String getPostal() { return postal; }

    public String getAddress() { return address; }

    public String getContact() { return contact; }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
