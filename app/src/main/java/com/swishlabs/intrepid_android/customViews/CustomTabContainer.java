package com.swishlabs.intrepid_android.customViews;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
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



public class CustomTabContainer extends RelativeLayout {

    private Context mContext;
    private int mTabAmount;
    private ViewPager mViewPager;
    private Activity mActivity;
    private TextView mTabSelector;

    private int tabWidth;
    private int tabHeight;

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

    public void createTabs(ArrayList<String> tabNames, ViewPager viewPager){
        mViewPager = viewPager;
        mTabAmount = tabNames.size();
        mTabSelector = (TextView)findViewById(R.id.tabSelector);
        LinearLayout layout = (LinearLayout)findViewById(R.id.tabLayout);
        tabWidth = this.getWidth()/mTabAmount;
        tabHeight = this.getHeight();
        LayoutParams selectorParams = new LayoutParams(tabWidth,tabHeight/16);
        selectorParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mTabSelector.setLayoutParams(selectorParams);
        mTabSelector.setWidth(tabWidth);

        for (int i = 0; i<mTabAmount; i++) {

            View child = LayoutInflater.from(mContext).inflate(
                    R.layout.individual_tab, null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tabWidth, tabHeight);
            params.width = tabWidth;
            params.height = tabHeight;
            child.setLayoutParams(params);

            layout.addView(child);
            TextView tabText = (TextView)child.findViewById(R.id.tabText);
            tabText.setText(tabNames.get(i));
            Log.e("CreateTab", tabNames.get(i));
            setListener(child, i);

        }


    }

    public void slideScrollIndicator(int position, int positionOffsetPixels){
        mTabSelector.setX(position*tabWidth+positionOffsetPixels/mTabAmount);
    }



    private void setListener(View view, final int position){
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(position);
            }
        });
    }







}
