package com.swishlabs.intrepid_android.data.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by wwang on 15-04-13.
 */
public class HealthCondition implements Serializable {
    public String id;
    public String name;
    public Content content;
    public Image images;

    public HealthCondition(JSONObject obj) throws JSONException {
        if(obj == null) {
            content = new Content(null);
            images = new Image(null);
            return;
        }

        id = obj.optString("id");
        name = obj.optString("name");

        if(obj.has("content")){
            content = new Content(obj.getJSONObject("content"));
        }else {
            content = new Content(null);
        }

        if(obj.has("images")){
            JSONObject im = obj.getJSONObject("images");
            if(im.has("general"))
                images= new Image(im.getJSONObject("general"));
        }else {
            images = new Image(null);
        }
    }

    public static class Content implements Serializable {
        public String description;
        public String symptoms;
        public String prevention;

        public Content(JSONObject obj) throws JSONException{
            if(obj == null)
                return;

            description = obj.optString("description");
            symptoms = obj.optString("symptoms");
            prevention = obj.optString("prevention");
        }
    }
}
