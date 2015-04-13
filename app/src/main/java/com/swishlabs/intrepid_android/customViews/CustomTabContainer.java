package com.swishlabs.intrepid_android.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swishlabs.intrepid_android.R;

import java.util.ArrayList;

/**
 * Created by ryanracioppo on 2015-04-13.
 */



public class CustomTabContainer extends LinearLayout {

    private Context mContext;
    private int mTabAmount;

    public CustomTabContainer(Context context) {
        super(context);
        mContext = context;
    }

    public CustomTabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CustomTabContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void createTabs(ArrayList<String> tabNames){

        mTabAmount = tabNames.size();

        final int tabWidth = this.getWidth()/mTabAmount;
        final int tabHeight = this.getHeight();
        for (int i = 0; i<mTabAmount; i++) {

            View child = LayoutInflater.from(mContext).inflate(
                    R.layout.individual_tab, null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tabWidth, tabHeight);
            params.width = tabWidth;
            params.height = tabHeight;
            child.setLayoutParams(params);
            this.addView(child);
            TextView tabText = (TextView)child.findViewById(R.id.tabText);
            tabText.setText(tabNames.get(i));
            Log.e("CreateTab", tabNames.get(i));
        }


    }

}
