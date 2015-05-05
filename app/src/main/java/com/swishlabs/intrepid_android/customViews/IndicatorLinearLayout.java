
package com.swishlabs.intrepid_android.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.swishlabs.intrepid_android.R;

import java.util.ArrayList;
import java.util.List;


public class IndicatorLinearLayout extends LinearLayout {

    private List<ImageView> points = new ArrayList<ImageView>();
    private Context context;
    private int selectedIndex;
    private ViewPager mViewPager;
    private int totalNum;

    public IndicatorLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public IndicatorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initPoints(int count, int selectedIndex, ViewPager viewPager) {
    	this.removeAllViews();
        this.selectedIndex = selectedIndex;
        this.mViewPager = viewPager;
        this.totalNum = count;

        for (int i = 0; i < count; i++) {
            ImageView point = new ImageView(context);
            point.setImageResource(R.drawable.home_indirector_point);
            point.setSelected(true);
            if (i == selectedIndex) {
                point.setAlpha(1.0F);
            } else {
                point.setAlpha(0.3F);
            }
            point.setTag(i);

            points.add(i, point);
            point.setOnClickListener( new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    mViewPager.setCurrentItem(index);
                }
            });

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 15;
            this.addView(points.get(i),params);
        }
        invalidate();
    }

    public void updatePageIndicator(int currentPage, float positionOffset) {

        int nextSelectedIndex = currentPage;
        if (positionOffset > 0) {
            nextSelectedIndex = currentPage + 1;
        }else if (positionOffset < 0){
            nextSelectedIndex = currentPage - 1;
        }
        ImageView currentPoint = points.get(currentPage);
        ImageView nextPoint = points.get(nextSelectedIndex);
        if (positionOffset!=0) {
            currentPoint.setAlpha(1.0F - Math.abs(positionOffset * 0.7F));
            nextPoint.setAlpha(0.3F + Math.abs(positionOffset * 0.7F));
        }
        this.selectedIndex = nextSelectedIndex;

    }


}
