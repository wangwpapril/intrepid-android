package com.swishlabs.intrepid_android.data.api.model;

import android.database.Cursor;

import com.swishlabs.intrepid_android.util.StringUtil;

/**
 * Created by wwang on 15-04-16.
 */
public class Currency {
    String currencyCode;
    String imageUrl;
    public Currency(String code, String url) {
        this.currencyCode = code;
        this.imageUrl = url;
    }

    public String getCurrencyCode() { return currencyCode; }
    public String getImageUrl() { return  imageUrl; }
}
