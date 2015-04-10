package com.swishlabs.intrepid_android.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.swishlabs.intrepid_android.R;
import com.swishlabs.intrepid_android.activity.SettingsActivity;
import com.swishlabs.intrepid_android.activity.TripPagesActivity;

/**
 * Created by ryanracioppo on 2015-04-09.
 */
public class IntrepidMenu extends ScrollView {


    public IntrepidMenu(Context context) {
        super(context);
    }

    public static void setupMenu(final Context context, final Activity activity){
        FrameLayout settingsButton = (FrameLayout)activity.findViewById(R.id.settings_menu_btn);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(context, SettingsActivity.class);
                activity.startActivity(mIntent);
            }
        });
        FrameLayout tripsButton = (FrameLayout)activity.findViewById(R.id.trips_menu_btn);
        tripsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(context, TripPagesActivity.class);
                activity.startActivity(mIntent);                
            }
        });

    }


}
