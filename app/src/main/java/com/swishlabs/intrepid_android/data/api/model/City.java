package com.swishlabs.intrepid_android.data.api.model;

import java.io.Serializable;

/**
 * Created by wwang on 15-08-04.
 */
public class City implements Serializable {

    String id;
    String name;
    String content;
    String longitude;
    String latitude;

    public City () {

    }

    public String getId () { return id; }

    public String getName () { return name; }

    public String getContent () { return content; }

    public String getLongitude() { return longitude; }

    public String getLatitude() { return latitude; }

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
}
